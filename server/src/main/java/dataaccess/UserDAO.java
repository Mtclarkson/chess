package dataaccess;

import model.*;

import java.util.ArrayList;

public interface UserDAO {
    UserData createUser(UserData user) throws DataAccessException;

    UserData getUser(String username) throws DataAccessException;

    ArrayList<UserData> usersList() throws DataAccessException;

    void clearAllUsers() throws DataAccessException;
}
