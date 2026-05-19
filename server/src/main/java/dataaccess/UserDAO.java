package dataaccess;

import model.*;

public interface UserDAO {
//    UserData createUser(String username, String password, String email) throws DataAccessException;
//
//    UserData getUser(String username, String password) throws DataAccessException;

    void clearAllUsers() throws DataAccessException;
}
