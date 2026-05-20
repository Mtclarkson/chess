package service;

import dataaccess.*;
import model.*;

public class UserService {
    private static UserDAO userDAO = null;

    public UserService(UserDAO userDAO) {
        UserService.userDAO = userDAO;
    }

    public static UserData createUser(UserData user) throws DataAccessException {
        return userDAO.createUser(user);
    }

    public static UserData getUser(String usernameGiven) throws DataAccessException {
        return userDAO.getUser(usernameGiven);
    }

    public static void clearAllUsers() throws DataAccessException {
        userDAO.clearAllUsers();
    }
}