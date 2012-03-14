package com.hd11.process.ihu;

import org.junit.Test;

import com.hd11.process.ihu.Ihu;
import com.senacor.hd11.model.User;
import com.senacor.hd11.model.UserApplication;
import com.senacor.hd11.model.User.UserState;


public class ProcessStartTest {
	@Test
	public void startTwoProcessSessions() throws InterruptedException {
		UserApplication userApp = new UserApplication();
		userApp.setAppuuid("00001");
		User user = new User();
		user.setFirstname("Hans");
		user.setLastname("Wurst");
		user.setState(UserState.PENDING);
		user.setPassword("Password");
		user.setUsername("HWurst");
		userApp.setUser(user);
		
		Ihu process = new Ihu();
		process.startProcess(userApp);
		Thread.sleep(2000);
		
		userApp = new UserApplication();
		userApp.setAppuuid("00002");
		User userPeter = new User();
		userPeter.setFirstname("Peter");
		userPeter.setLastname("Wurst");
		userPeter.setState(UserState.PENDING);
		userPeter.setPassword("Password");
		userPeter.setUsername("PWurst");
		userApp.setUser(userPeter);
		
		process.startProcess(userApp);
		Thread.sleep(2000);

	}
}
