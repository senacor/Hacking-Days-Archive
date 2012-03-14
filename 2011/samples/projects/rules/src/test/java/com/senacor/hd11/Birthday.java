package com.senacor.hd11;

/**
 * Ralph Winzinger, Senacor
 */
public class Birthday {
    private Person person;
    private boolean celebrated = false;

    public Birthday(Person person) {
        this.person = person;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public boolean isCelebrated() {
        return celebrated;
    }

    public void setCelebrated(boolean celebrated) {
        this.celebrated = celebrated;
    }
}
