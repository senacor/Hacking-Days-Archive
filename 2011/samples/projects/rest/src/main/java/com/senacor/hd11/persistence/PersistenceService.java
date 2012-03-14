package com.senacor.hd11.persistence;

import com.senacor.hd11.model.User;
import com.senacor.hd11.model.UserApplication;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

/**
 * Ralph Winzinger, Senacor
 */
public class PersistenceService {
    private static PersistenceService ourInstance = new PersistenceService();

    private HashMap<String, User> users = new HashMap<String, User>();
    private HashMap<String, UserApplication> applications = new HashMap<String, UserApplication>();

    public static PersistenceService getInstance() {
        return ourInstance;
    }

    private PersistenceService() {
    }

    // UserApplications

    public void addApplication(String uuid, UserApplication userApplication) throws UserExistsException {
        String username = userApplication.getUser().getUsername();

        Collection<User> activeUsers = users.values();
        for (User user : activeUsers) {
            if (user.getUsername().equalsIgnoreCase(username)) {
                throw new UserExistsException();
            }
        }
        Collection<UserApplication> pendingUsers = applications.values();
        for (UserApplication pendingUser : pendingUsers) {
            if (pendingUser.getUser().getUsername().equalsIgnoreCase(username)) {
                throw new UserExistsException();
            }
        }

        applications.put(uuid, userApplication);
        users.put(userApplication.getUser().getUsername(), userApplication.getUser());
    }

    public UserApplication getApplication(String uuid) throws NoSuchUserException {
        UserApplication userApplication = applications.get(uuid);

        if (userApplication == null) {
            throw new NoSuchUserException();
        }

        return userApplication;
    }

    public void updateApplication(String uuid, UserApplication userApplication) throws NoSuchUserException {
        UserApplication existingApplication = applications.get(uuid);

        if (existingApplication == null) {
            throw new NoSuchUserException();
        }

        applications.put(uuid, userApplication);
        if (userApplication.getState() == UserApplication.ApplicationState.ACCEPTED) {
            System.err.println("setting user '"+userApplication.getUser().getUsername()+"' active");
            userApplication.getUser().setState(User.UserState.ACTIVE);
            users.put(userApplication.getUser().getUsername(), userApplication.getUser());
        } else if (userApplication.getState() == UserApplication.ApplicationState.ACCEPTED) {
            users.remove(userApplication.getUser().getUsername());
        }
    }

    // User

    public User getUser(String username) {
        return users.get(username);
    }

    public List<User> getUsers() {
        return new ArrayList<User>(users.values());
    }
}
