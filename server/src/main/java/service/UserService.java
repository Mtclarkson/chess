package service;

import dataaccess.*;
import model.*;

import java.util.ArrayList;

public class UserService {
    private static UserDAO userDAO = null;

    public UserService(UserDAO userDAO) {
        UserService.userDAO = userDAO;
    }

    private boolean isNullOrBlank(String input) {
        return (input == null) || (input.isEmpty());
    }

    public UserData createUser(UserData user) throws DataAccessException {
        if (isNullOrBlank(user.username())
                || isNullOrBlank(user.password())
                || isNullOrBlank(user.email())) {
            return user;
        } else if (userDAO.getUser(user.username())!=null) {
            return user;
        }
        return userDAO.createUser(user);
    }

    public UserData getUser(String usernameGiven) throws DataAccessException {
        if (isNullOrBlank(usernameGiven)) {
            return null;
        }
        return userDAO.getUser(usernameGiven);
    }

    public void clearAllUsers() throws DataAccessException {
        userDAO.clearAllUsers();
    }

    // for JUnit testing only
    public ArrayList<UserData> usersList() throws DataAccessException {
        return userDAO.usersList();
    }
}