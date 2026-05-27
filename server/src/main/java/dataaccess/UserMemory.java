package dataaccess;

import model.UserData;

import java.util.ArrayList;

public class UserMemory implements UserDAO {
    final private ArrayList<UserData> users = new ArrayList<>();

    private boolean isNullOrBlank(String input) {
        return (input == null) || (input.isEmpty());
    }

    public UserData createUser(UserData user) throws DataAccessException {
        if (isNullOrBlank(user.username()) || isNullOrBlank(user.password())) {
            return null;
        }
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

    public ArrayList<UserData> usersList() {
        return users;
    }

    public void clearAllUsers() {users.clear();}
}
