package com.senacor.hd11.model;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Ralph Winzinger, Senacor
 */
@XmlRootElement
public class Message implements Serializable {

	private static final long serialVersionUID = 1L;
	private String username;
    private String msguuid;
    private String text;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMsguuid() {
        return msguuid;
    }

    public void setMsguuid(String msguuid) {
        this.msguuid = msguuid;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
