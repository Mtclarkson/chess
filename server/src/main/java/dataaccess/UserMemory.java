package dataaccess;

import model.UserData;

import java.util.ArrayList;

public class UserMemory implements UserDAO {
    final private ArrayList<UserData> users = new ArrayList<>();

    public UserData createUser(UserData user) throws DataAccessException {
        user = new UserData(user.username(), user.password(), user.email());
        users.add(user);
        return user;
    }

    public UserData getUser(String usernameGiven) throws DataAccessException {
        for (UserData user : users) {
            if (user.username().equals(usernameGiven)) {
                return user;
            }
        }
        return null;
    }

    public void clearAllUsers() {users.clear();}
}
