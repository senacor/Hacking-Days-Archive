package com.senacor.hd11.messages;

/**
 * Ralph Winzinger, Senacor
 */
public class Strike {
    private String username;
    private int strikes = 1;

    public Strike(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getStrikes() {
        return strikes;
    }

    public void setStrikes(int strikes) {
        this.strikes = strikes;
    }
}
