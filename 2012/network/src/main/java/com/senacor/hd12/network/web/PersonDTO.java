package com.senacor.hd12.network.web;

public class PersonDTO {
	String username;
	String iconLink;
	Long numFollowers;
	
	public PersonDTO(String username, String iconLink, Long numFollowers) {
		this.username = username;
		this.iconLink = iconLink;
		this.numFollowers = numFollowers;
	}
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getIconLink() {
		return iconLink;
	}
	public void setIconLink(String iconLink) {
		this.iconLink = iconLink;
	}
	public Long getNumFollowers() {
		return numFollowers;
	}
	public void setNumFollowers(Long numFollowers) {
		this.numFollowers = numFollowers;
	}
	
	
}
