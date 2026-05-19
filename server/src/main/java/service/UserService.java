package service;

import dataaccess.*;
import model.*;

public class UserService {
    private static UserDAO userDAO = null;

    public UserService(UserDAO userDAO) {
        UserService.userDAO = userDAO;
    }

    public static void clearAllUsers() throws DataAccessException {
        userDAO.clearAllUsers();
    }
}