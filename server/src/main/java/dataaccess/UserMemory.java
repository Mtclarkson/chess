package dataaccess;

import model.UserData;

import java.util.HashMap;

public class UserMemory implements UserDAO {
    final private HashMap<Integer, UserData> users = new HashMap<>();
}
