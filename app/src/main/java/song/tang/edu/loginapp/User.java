package song.tang.edu.loginapp;

public class User {

    private String firstName;
    private String lastName;
    private String uID;

    public User() {

    }
    public User(String firstName, String lastName, String uID) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.uID = uID;
    }

    public String getFirstName() {
        return firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public String getFullName() {
        return firstName + " " + lastName;
    }
    public String getuID() {
        return uID;
    }
}
