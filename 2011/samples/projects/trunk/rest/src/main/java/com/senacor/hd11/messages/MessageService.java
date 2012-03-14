package com.senacor.hd11.messages;

import com.senacor.hd11.model.Message;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.HashMap;

/**
 * Ralph Winzinger, Senacor
 */
public class MessageService {
    private static MessageService ourInstance = new MessageService();

    private HashMap<String, HashMap<String, Message>> messagesByUser = new HashMap<String, HashMap<String, Message>>();
    private HashMap<String, Message> messagesByUuid = new HashMap<String, Message>();

    public static MessageService getInstance() {
        return ourInstance;
    }

    private MessageService() {
        System.out.println("starting messageService");
        ProcessEngine.getInstance().insertStrikesRule(new Strikes(3));
        ProcessEngine.getInstance().insertNiceText(new NiceMessage());
    }

    public Message createMessage(Message message) {
        messagesByUuid.put(message.getMsguuid(), message);
        HashMap<String, Message> userMessages = messagesByUser.get(message.getUsername());
        if (userMessages == null) {
            userMessages = new HashMap<String, Message>();
            messagesByUser.put(message.getUsername(), userMessages);
        }

        userMessages.put(message.getMsguuid(), message);
        AdminMsg adminMsg = new AdminMsg();
        adminMsg.setContent(message.getText());
        adminMsg.setAuthor(message.getUsername());

        Message checkedMessage = ProcessEngine.getInstance().checkMessage(message);
        if (checkedMessage.getText().equalsIgnoreCase(adminMsg.getContent())) {
            adminMsg.setBlocked(false);
        } else {
            adminMsg.setBlocked(true);
        }
        sendMessageToAdmins(adminMsg);

        return checkedMessage;
    }

    private void sendMessageToAdmins(AdminMsg adminMsg) {
        ClientConfig config = new DefaultClientConfig();
		Client client = Client.create(config);
        ClientResponse response = client.resource("http://192.168.2.5:8080/Stats/messagehandler").post(ClientResponse.class, adminMsg);

        if (response.getStatus() != ClientResponse.Status.CREATED.getStatusCode()) {
            // throw new RuntimeException("something failed: "+response.getEntity(String.class));
            System.err.println("ADMIN-MSG failed");
        }
    }

    @XmlRootElement
    private static class AdminMsg {
        private String content;
        private boolean blocked;
        private String author;

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public boolean isBlocked() {
            return blocked;
        }

        public void setBlocked(boolean blocked) {
            this.blocked = blocked;
        }

        public String getAuthor() {
            return author;
        }

        public void setAuthor(String author) {
            this.author = author;
        }
    }
}
