package com.senacor.hd11.model;

import javax.xml.bind.annotation.XmlRootElement;

import java.io.Serializable;
import java.util.Date;

/**
 * Ralph Winzinger, Senacor
 */
@XmlRootElement
public class User implements Serializable {

	private static final long serialVersionUID = 1L;

	public enum UserState {PENDING, ACTIVE, MUTED}

    private UserState state;
    private String username;
    private String password;
    private String firstname;
    private String lastname;
    private Date dateOfBirth;

    public UserState getState() {
        return state;
    }

    public void setState(UserState state) {
        this.state = state;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }
}
