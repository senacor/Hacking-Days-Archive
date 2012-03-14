package com.hd11.process.ihu;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.drools.SystemEventListenerFactory;
import org.drools.process.workitem.wsht.BlockingGetTaskResponseHandler;
import org.drools.task.AccessType;
import org.drools.task.Content;
import org.drools.task.TaskData;
import org.drools.task.query.TaskSummary;
import org.drools.task.service.ContentData;
import org.drools.task.service.TaskClient;
import org.drools.task.service.mina.MinaTaskClientConnector;
import org.drools.task.service.mina.MinaTaskClientHandler;
import org.drools.task.service.responsehandlers.BlockingGetContentResponseHandler;
import org.drools.task.service.responsehandlers.BlockingTaskOperationResponseHandler;
import org.drools.task.service.responsehandlers.BlockingTaskSummaryResponseHandler;

import com.senacor.hd11.model.UserApplication;

public class TaskServerCommunication {

	/**
	 * UUID,TaskSummary
	 */
	private HashMap<String, TaskSummary> taskSummaries = new HashMap<String, TaskSummary>();
	/**
	 * <AdminName,TaskClient>
	 */
	private HashMap<String, TaskClient> taskClients = new HashMap<String, TaskClient>();

	public void startAndCompleteTaskForUUID(String admin, String appUUID, UserApplication userApplication) {
		TaskSummary task = this.getTaskSummaryFromCacheByUUID(appUUID);
		startTask(admin, task);
		compeleteTask(admin, task, userApplication);
		this.removeTaskSummaryFromCache(appUUID);
	}

	public TaskSummary getTaskSummaryFromCacheByUUID(String appUUID) {
		return taskSummaries.get(appUUID);
	}

	public void startTask(String admin, TaskSummary task) {
		System.out.println("Start Task " + task.getId() + " by " + admin);
		TaskClient client = this.getTaskClientForAdmin(admin);
		BlockingTaskOperationResponseHandler operationHandler = new BlockingTaskOperationResponseHandler();
		client.start(task.getId(), admin, operationHandler);
		System.out.println("Task startetd... "
				+ operationHandler.waitTillDone(100000));
	}

	public void compeleteTask(String admin, TaskSummary task, UserApplication userApplication) {
		System.out.println("Start Task completion for " + task.getId() + " by "
				+ admin);
		
		TaskClient client = getTaskClientForAdmin(admin);
		BlockingTaskOperationResponseHandler operationHandler = new BlockingTaskOperationResponseHandler();
		
		ContentData contentData = new ContentData();
//		contentData.setType("com.senacor.hd11.model.UserApplication");
		contentData.setType("java.util.HashMap");
		HashMap<String,Object> map = new HashMap<String, Object>();
		map.put("result", userApplication);
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutputStream out;
		try {
			out = new ObjectOutputStream(bos);
			out.writeObject(map);
			out.close();
			contentData.setContent(bos.toByteArray());
			contentData.setAccessType(AccessType.Inline);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		
		client.complete(task.getId(), admin, contentData, operationHandler);
		System.out.println("Task completed... "
				+ operationHandler.waitTillDone(10000));
	}

	public void releaseTask(String admin, TaskSummary task) {
		System.out.println("Release Task " + task.getId() + " by " + admin);
		TaskClient client = getTaskClientForAdmin(admin);
		BlockingTaskOperationResponseHandler operationHandler = new BlockingTaskOperationResponseHandler();
		client.release(task.getId(), admin, operationHandler);
		System.out.println("Task release... "
				+ operationHandler.waitTillDone(10000));
	}

	public void removeTaskSummaryFromCache(String appUUID) {
		taskSummaries.remove(appUUID);
	}

	public TaskClient getTaskClientForAdmin(String admin) {
		// TODO FIXME
		// TaskClient client = taskClients.get(admin);
		// if(client == null){
		// System.out.println("Create TaskClient for Admin "+admin+"...");
		// client = createTaskClient(admin);
		// taskClients.put(admin,client);
		// }

		System.out.println("Return TaskClient for " + admin);

		return createTaskClient(admin);
		// return client;
	}

	public TaskClient createTaskClient(String admin) {
		TaskClient client = new TaskClient(new MinaTaskClientConnector(admin,
				new MinaTaskClientHandler(SystemEventListenerFactory
						.getSystemEventListener())));
		client.connect("127.0.0.1", 9123);
		return client;
	}

	public List<UserApplication> getTasksByOwner(String admin) {
		System.out.println("Get all Tasks for Admin " + admin);
		TaskClient client = getTaskClientForAdmin(admin);
		List<UserApplication> taskCollection = new LinkedList<UserApplication>();
		try {
			BlockingTaskSummaryResponseHandler summaryHandler = new BlockingTaskSummaryResponseHandler();
			client.getTasksAssignedAsPotentialOwner(admin, "en-UK",
					summaryHandler);
			// lesen aller Tasks
			List<TaskSummary> tasks = summaryHandler.getResults();
			for (TaskSummary taskSummary : tasks) {
				System.out.println(taskSummary.getId() + " : "
						+ taskSummary.getName());

				BlockingGetTaskResponseHandler getTaskHandler = new BlockingGetTaskResponseHandler();
				client.getTask(taskSummary.getId(), getTaskHandler);
				TaskData data = getTaskHandler.getTask().getTaskData();

				BlockingGetContentResponseHandler getContentHandler = new BlockingGetContentResponseHandler();
				client.getContent(data.getDocumentContentId(),
						getContentHandler);
				Content content = getContentHandler.getContent();

				ByteArrayInputStream bis = new ByteArrayInputStream(content
						.getContent());
				ObjectInputStream ois = new ObjectInputStream(bis);

				Object obj = ois.readObject();
				UserApplication userApp = (UserApplication) obj;
				taskCollection.add(userApp);

				// cache
				this.taskSummaries.put(userApp.getAppuuid(), taskSummary);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				client.disconnect();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return taskCollection;
	}

}
