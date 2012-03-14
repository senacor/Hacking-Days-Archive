package com.senacor.hd11.messages;

/**
 * Ralph Winzinger, Senacor
 */
public class Strikes {
    private int numberOfStrikes;

    public Strikes(int numberOfStrikes) {
        this.numberOfStrikes = numberOfStrikes;
    }

    public int getNumberOfStrikes() {
        return numberOfStrikes;
    }

    public void setNumberOfStrikes(int numberOfStrikes) {
        this.numberOfStrikes = numberOfStrikes;
    }
}
