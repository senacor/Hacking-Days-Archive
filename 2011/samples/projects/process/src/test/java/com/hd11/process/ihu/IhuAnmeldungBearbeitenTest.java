package com.hd11.process.ihu;

import java.util.List;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import junit.framework.TestCase;

import org.drools.SystemEventListenerFactory;
import org.drools.event.process.ProcessCompletedEvent;
import org.drools.event.process.ProcessEventListener;
import org.drools.event.process.ProcessNodeLeftEvent;
import org.drools.event.process.ProcessNodeTriggeredEvent;
import org.drools.event.process.ProcessStartedEvent;
import org.drools.task.service.TaskService;
import org.drools.task.service.TaskServiceSession;
import org.drools.task.service.mina.MinaTaskServer;
import org.junit.Test;

import com.senacor.hd11.model.User;
import com.senacor.hd11.model.UserApplication;
import com.senacor.hd11.model.User.UserState;


public class IhuAnmeldungBearbeitenTest extends TestCase {

	private String admin = "Fix";
	private TaskService taskService; 
	private TaskServiceSession taskSession; 
	private MinaTaskServer server; 
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
//		this.start();
	}
	
	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
//		this.stop();
	}
	
	@Test
	public void testAnmeldungBearbeiten() throws Exception{
		
		UserApplication userApp = new UserApplication();
		userApp.setState(UserApplication.ApplicationState.PENDING);
		userApp.setAppuuid("00001");
		User user = new User();
		user.setFirstname("Hans");
		user.setLastname("Wurst");
		user.setState(UserState.PENDING);
		user.setPassword("Password");
		user.setUsername("HWurst");
		userApp.setUser(user);
		// Prozess starten...
		Ihu process = new Ihu(new FinishedAnmeldungBearbeitenProcess());
		process.startProcess(userApp);
		Thread.sleep(5000);
		
		// Alle Tasks fuer den Admin holen...
		TaskServerCommunication server = new TaskServerCommunication();
		List<UserApplication> tasks = server.getTasksByOwner("Foxi");
		assertEquals(1,tasks.size());
		assertEquals(tasks.get(0).getAppuuid(),userApp.getAppuuid());
		// Freigeben
		userApp.setState(UserApplication.ApplicationState.ACCEPTED);
		server.startAndCompleteTaskForUUID("Foxi", tasks.get(0).getAppuuid(), userApp);
		Thread.sleep(2000);
		
		List<UserApplication> tasksFix = server.getTasksByOwner("Fix");
		assertEquals(1,tasksFix.size());
		assertEquals(tasksFix.get(0).getAppuuid(),userApp.getAppuuid());
		// Freigeben
		server.startAndCompleteTaskForUUID("Fix", tasksFix.get(0).getAppuuid(), userApp);
		Thread.sleep(2000);
		
	}
	
	private void start() throws Exception { 
		  EntityManagerFactory emf = Persistence.createEntityManagerFactory("org.drools.task"); 

		  taskService = new TaskService(emf, SystemEventListenerFactory.getSystemEventListener()); 

		  taskSession = taskService.createSession(); 

		  taskSession.addUser(new org.drools.task.User("Administrator"));
		  taskSession.addUser(new org.drools.task.User(admin));
		 
		  server = new MinaTaskServer( taskService );
		  Thread thread = new Thread( server );
		  thread.start();
		  
		  System.out.println("Server started ... Sleeping 5s..."); 
		  Thread.sleep(5000);
	 } 
	
	private void stop() throws Exception{
		server.stop();
		System.out.println("Server stoped...");
	}
	
	
	
}
