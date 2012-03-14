package com.senacor.hd11.listener;

import com.senacor.hd11.persistence.NoSuchUserException;
import com.senacor.hd11.persistence.PersistenceService;
import org.drools.event.process.ProcessCompletedEvent;
import org.drools.event.process.ProcessEventListener;
import org.drools.event.process.ProcessNodeLeftEvent;
import org.drools.event.process.ProcessNodeTriggeredEvent;
import org.drools.event.process.ProcessStartedEvent;
import org.drools.runtime.process.ProcessInstance;
import org.drools.runtime.process.WorkflowProcessInstance;

import com.senacor.hd11.model.UserApplication;

public class FinishedAnmeldungBearbeitenProcess implements ProcessEventListener {

	@Override
	public void afterNodeLeft(ProcessNodeLeftEvent event) {
		// TODO Auto-generated method stub

	}

	@Override
	public void afterNodeTriggered(ProcessNodeTriggeredEvent event) {
		// TODO Auto-generated method stub

	}

	@Override
	public void afterProcessCompleted(ProcessCompletedEvent event) {
		WorkflowProcessInstance process = (WorkflowProcessInstance) event.getProcessInstance();
		if (process.getState() == ProcessInstance.STATE_COMPLETED) {
			UserApplication userApplication = (UserApplication) process.getVariable("userApplication");
            System.err.println("process finished for uuid "+userApplication.getAppuuid()+" ("+userApplication.getState()+")");

            if (userApplication.getState() == UserApplication.ApplicationState.PENDING) {
                System.err.println("PENDING?!?! overriding!");
                userApplication.setState(UserApplication.ApplicationState.ACCEPTED);
            }
            try {
                PersistenceService.getInstance().updateApplication(userApplication.getAppuuid(), userApplication);
            } catch (NoSuchUserException e) {
                // should not happen
            }
        } else {
			// TODO Do other stuff
		}
	}

	@Override
	public void afterProcessStarted(ProcessStartedEvent event) {
		// TODO Auto-generated method stub

	}

	@Override
	public void beforeNodeLeft(ProcessNodeLeftEvent event) {
		// TODO Auto-generated method stub

	}

	@Override
	public void beforeNodeTriggered(ProcessNodeTriggeredEvent event) {
		// TODO Auto-generated method stub

	}

	@Override
	public void beforeProcessCompleted(ProcessCompletedEvent event) {
		// TODO Auto-generated method stub

	}

	@Override
	public void beforeProcessStarted(ProcessStartedEvent event) {
		// TODO Auto-generated method stub

	}

}
