package com.senacor.hd11.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Users {

	private static final long serialVersionUID = 1L;

	private User[] user;

	public User[] getUser() {
		return user;
	}

	public void setUser(User[] user) {
		this.user = user;
	}

}
