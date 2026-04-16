package services;

import java.util.ArrayList;
import java.util.List;
import models.User;
import utils.FileUtil;

public class UserService {
    private final String userFilePath;

    public UserService(String userFilePath) {
        this.userFilePath = userFilePath;
    }

    public boolean registerUser(String username, String password, String role) {
        List<User> users = getAllUsers();

        for (User user : users) {
            if (user.getUsername().equalsIgnoreCase(username)) {
                return false;
            }
        }

        users.add(new User(username, password, role));
        saveUsers(users);
        return true;
    }

    public User login(String username, String password) {
        for (User user : getAllUsers()) {
            if (user.getUsername().equalsIgnoreCase(username)
                    && user.getPassword().equals(password)) {
                return user;
            }
        }
        return null;
    }

    public List<User> getAllUsers() {
        List<String> lines = FileUtil.readLines(userFilePath);
        List<User> users = new ArrayList<>();

        for (String line : lines) {
            users.add(User.fromFileString(line));
        }
        return users;
    }

    private void saveUsers(List<User> users) {
        List<String> lines = new ArrayList<>();

        for (User user : users) {
            lines.add(user.toFileString());
        }
        FileUtil.writeLines(userFilePath, lines);
    }
}
