public class User {

    String email;
    String password;
    String role;
    double credit = 1000;
    int points = 0;
    double totalSpent = 0;

    public User(String email, String password, String role) {
        this.email = email;
        this.password = password;
        this.role = role;
    }
}