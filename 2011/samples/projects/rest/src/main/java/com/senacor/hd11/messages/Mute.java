package com.senacor.hd11.messages;

/**
 * Ralph Winzinger, Senacor
 */
public class Mute {
    private String username;

    public Mute(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
