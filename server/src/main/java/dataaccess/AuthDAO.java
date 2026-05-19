package dataaccess;

import model.AuthData;

public interface AuthDAO {

    void clearAllAuthTokens() throws DataAccessException;
}
