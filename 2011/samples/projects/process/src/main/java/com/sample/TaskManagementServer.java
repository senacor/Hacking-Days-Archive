package com.sample;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.drools.SystemEventListenerFactory;
import org.drools.task.User;
import org.drools.task.service.TaskService;
import org.drools.task.service.TaskServiceSession;
import org.drools.task.service.mina.MinaTaskServer;

import com.sun.jmx.snmp.tasks.Task;

public class TaskManagementServer {
	private TaskService taskService; 
	private TaskServiceSession taskSession; 
	private MinaTaskServer server; 

    public static void main(String[] args) throws Exception { 
	  new TaskManagementServer().start(); 
	} 

	private void start() throws Exception { 
	  EntityManagerFactory emf = Persistence.createEntityManagerFactory("org.drools.task"); 

	  taskService = new TaskService(emf, SystemEventListenerFactory.getSystemEventListener()); 

	  taskSession = taskService.createSession(); 

	  taskSession.addUser(new User("Administrator"));
	  taskSession.addUser(new User("alice"));
	  taskSession.addUser(new User("bob"));
	  taskSession.addUser(new User("chris"));
	  taskSession.addUser(new User("danny"));
	  taskSession.addUser(new User("Fix"));
	  taskSession.addUser(new User("Foxy"));
	 
	  MinaTaskServer server = new MinaTaskServer( taskService );
	  Thread thread = new Thread( server );
	  thread.start();

	  System.out.println("Server started ..."); 
    } 
}
