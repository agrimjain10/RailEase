package models;
public class User {
    private String username;
    private String password;
    private String role;

    public User(String username, String password, String role)
    {
        this.username = username;
        this.password = password;
        this.role = role;
    }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getRole() { return role; }

    public  String toFileString() 
    {
        return username + "," + password + "," + role;
    }
    public static User fromFileStrng(String line)
    {
        String[] parts = line.split(",");
        return new User (parts[0], parts[1], parts[2]);
    }
}
