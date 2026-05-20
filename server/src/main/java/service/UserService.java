package service;

import dataaccess.*;
import model.*;

public class UserService {
    private static UserDAO userDAO = null;

    public UserService(UserDAO userDAO) {
        UserService.userDAO = userDAO;
    }

    public UserData createUser(UserData user) throws DataAccessException {
        return userDAO.createUser(user);
    }

    public UserData getUser(String usernameGiven) throws DataAccessException {
        return userDAO.getUser(usernameGiven);
    }

    public void clearAllUsers() throws DataAccessException {
        userDAO.clearAllUsers();
    }
}