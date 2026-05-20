package dataaccess;

import model.*;

public interface UserDAO {
    UserData createUser(UserData user) throws DataAccessException;

    UserData getUser(String username) throws DataAccessException;

    void clearAllUsers() throws DataAccessException;
}
