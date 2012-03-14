package com.senacor.hd11.messages;

import com.senacor.hd11.model.Message;

/**
 * Ralph Winzinger, Senacor
 */
public class MsgCheck {
    private Message message;

    public MsgCheck(Message message) {
        this.message = message;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }
}
