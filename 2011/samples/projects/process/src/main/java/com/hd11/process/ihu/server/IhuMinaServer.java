package com.hd11.process.ihu.server;

import org.drools.SystemEventListenerFactory;
import org.drools.task.service.TaskService;
import org.drools.task.service.mina.MinaTaskServerHandler;

public class IhuMinaServer extends IhuBaseServer implements Runnable {
	
	public IhuMinaServer(TaskService service) {
		super(new MinaTaskServerHandler(service, SystemEventListenerFactory
				.getSystemEventListener()), 9123,"0.0.0.0");
	}

}
