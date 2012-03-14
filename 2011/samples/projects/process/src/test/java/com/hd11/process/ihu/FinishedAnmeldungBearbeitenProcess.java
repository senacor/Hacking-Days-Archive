package com.hd11.process.ihu;

import org.drools.event.process.ProcessCompletedEvent;
import org.drools.event.process.ProcessEventListener;
import org.drools.event.process.ProcessNodeLeftEvent;
import org.drools.event.process.ProcessNodeTriggeredEvent;
import org.drools.event.process.ProcessStartedEvent;
import org.drools.runtime.process.ProcessInstance;
import org.drools.runtime.process.WorkflowProcessInstance;

import com.senacor.hd11.model.UserApplication;

public class FinishedAnmeldungBearbeitenProcess implements ProcessEventListener {

	public void afterProcessCompleted(ProcessCompletedEvent event) {
		WorkflowProcessInstance process = (WorkflowProcessInstance) event.getProcessInstance();
		if (process.getState() == ProcessInstance.STATE_COMPLETED) {
			UserApplication userApplication = (UserApplication) 
				process.getVariable("userApplication");
			System.out.println(userApplication.getAppuuid()+" ("+userApplication.getState()+")");
			// TODO Do stuff			
		} else {
			// TODO Do other stuff
		}
		System.out.println("");
	}

	public void afterNodeLeft(ProcessNodeLeftEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public void afterNodeTriggered(ProcessNodeTriggeredEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public void afterProcessStarted(ProcessStartedEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public void beforeNodeLeft(ProcessNodeLeftEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public void beforeNodeTriggered(ProcessNodeTriggeredEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public void beforeProcessCompleted(ProcessCompletedEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public void beforeProcessStarted(ProcessStartedEvent arg0) {
		// TODO Auto-generated method stub
		
	}

}
