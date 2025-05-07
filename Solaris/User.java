package Solaris;

public class User {
    int userID;
    String userName;
    String email;

    public User(int id, String name, String email) {
        userID = id;
        userName = name;
        this.email = email;
    }

    public int getUserID() {
        return userID;
    }

    public String getUserName() {
        return userName;
    }

    public String getEmail() {
        return email;
    }
}
