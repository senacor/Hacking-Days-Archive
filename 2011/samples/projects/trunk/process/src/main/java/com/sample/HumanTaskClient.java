package com.sample;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.List;

import org.drools.SystemEventListenerFactory;
import org.drools.task.Content;
import org.drools.task.Task;
import org.drools.task.TaskData;
import org.drools.task.query.TaskSummary;
import org.drools.task.service.ContentData;
import org.drools.task.service.TaskClient;
import org.drools.task.service.mina.MinaTaskClientConnector;
import org.drools.task.service.mina.MinaTaskClientHandler;
import org.drools.task.service.responsehandlers.BlockingGetContentResponseHandler;
import org.drools.task.service.responsehandlers.BlockingGetTaskResponseHandler;
import org.drools.task.service.responsehandlers.BlockingTaskOperationResponseHandler;
import org.drools.task.service.responsehandlers.BlockingTaskSummaryResponseHandler;

public class HumanTaskClient {
	public static void main(String[] args) {
		TaskClient client = new TaskClient(
			new MinaTaskClientConnector("alice", new MinaTaskClientHandler(SystemEventListenerFactory.getSystemEventListener()))
		);
		
		try {
			client.connect("127.0.0.1", 9123);
			System.out.println("connecting to task mgmt server and requesting tasks (for alice) ...");
			BlockingTaskSummaryResponseHandler summaryHandler = new BlockingTaskSummaryResponseHandler();
			client.getTasksAssignedAsPotentialOwner("alice", "en-UK", summaryHandler);
			List<TaskSummary> tasks = summaryHandler.getResults();
			TaskSummary task = null;
			for (TaskSummary taskSummary : tasks) {
				System.out.println("Task "+taskSummary.getId() + " : " + taskSummary.getName());
				task = taskSummary;
			}
			if (task != null) {
				BlockingTaskOperationResponseHandler operationHandler = new BlockingTaskOperationResponseHandler();
				client.start(task.getId(), "alice", operationHandler);
				operationHandler.waitTillDone(100000);
				System.out.println("started task "+task.getId());
				Thread.sleep(1000l);

				BlockingGetTaskResponseHandler handlerT = new BlockingGetTaskResponseHandler();
				client.getTask(task.getId(), handlerT);
				Task task2 = handlerT.getTask();
				TaskData taskData = task2.getTaskData(); 
				
				BlockingGetContentResponseHandler handlerC = new BlockingGetContentResponseHandler();
				client.getContent(taskData.getDocumentContentId(), handlerC);
				Content content = handlerC.getContent();
				System.out.println("Content= "+content);
				
				ByteArrayInputStream bais = new ByteArrayInputStream(content.getContent());
				ObjectInputStream ois = new ObjectInputStream(bais);
				Object obj = ois.readObject();
				
				operationHandler = new BlockingTaskOperationResponseHandler();
				ContentData cd = new ContentData();
				HashMap<String, String> map = new HashMap<String, String>();
				cd.setType("java.util.HashMap");
				map.put("result", "Das ist das Ergebnis vom HT");
				
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				try {
				    ObjectOutputStream oos = new ObjectOutputStream(bos); 
				    oos.writeObject(obj);
				    oos.flush();
				    oos.close(); 
				    cd.setContent(bos.toByteArray());
				    bos.close();
			    } catch (IOException ex) {
			    	ex.printStackTrace();
			    }
				client.complete(task.getId(), "alice", cd, operationHandler);
				operationHandler.waitTillDone(100000);
			}

			client.disconnect();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				client.disconnect();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}