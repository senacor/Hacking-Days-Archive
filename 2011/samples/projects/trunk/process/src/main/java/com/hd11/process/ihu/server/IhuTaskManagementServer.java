package com.hd11.process.ihu.server;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.drools.SystemEventListenerFactory;
import org.drools.task.User;
import org.drools.task.service.TaskService;
import org.drools.task.service.TaskServiceSession;
import org.drools.task.service.mina.MinaTaskServer;

public class IhuTaskManagementServer {
	private TaskService taskService; 
	private TaskServiceSession taskSession; 
	private MinaTaskServer server; 

    public static void main(String[] args) throws Exception { 
	  new IhuTaskManagementServer().start(); 
	} 

	private void start() throws Exception { 
	  EntityManagerFactory emf = Persistence.createEntityManagerFactory("org.drools.task"); 

	  taskService = new TaskService(emf, SystemEventListenerFactory.getSystemEventListener()); 

	  taskSession = taskService.createSession(); 

	  taskSession.addUser(new User("Administrator"));
	  taskSession.addUser(new User("Foxi"));
	  taskSession.addUser(new User("Fix"));
	 
	  IhuMinaServer server = new IhuMinaServer( taskService );
	  
	  Thread thread = new Thread( server );
	  thread.start();
	  
	  System.out.println("Server started ..."); 
    } 
}
