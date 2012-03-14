package com.senacor.hd11.model;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Ralph Winzinger, Senacor
 */
@XmlRootElement
public class UserApplication implements Serializable {

	private static final long serialVersionUID = 1L;

	public enum ApplicationState {PENDING, ACCEPTED, REJECTED, REWORK}

    private User user;
    private ApplicationState state;
    private String appuuid;
    private String comment;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public ApplicationState getState() {
        return state;
    }

    public void setState(ApplicationState state) {
        this.state = state;
    }

    public String getAppuuid() {
        return appuuid;
    }

    public void setAppuuid(String appuuid) {
        this.appuuid = appuuid;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
