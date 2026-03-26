package model;

public class User {
    private String email;
    private String password;
    private String name;
    private Role role;
    private double securityDeposit;

    public enum Role { ADMIN, BORROWER }

    public User(String email, String password, String name, Role role) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.role = role;
        this.securityDeposit = (role == Role.BORROWER) ? 30000.0 : 0.0;
    }

    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public String getName() { return name; }
    public Role getRole() { return role; }
    public double getSecurityDeposit() { return securityDeposit; }
    public void setSecurityDeposit(double amount) { this.securityDeposit = amount; }
    public void deductDeposit(double amount) { this.securityDeposit -= amount; }
}
