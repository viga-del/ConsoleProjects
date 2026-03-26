import java.util.Optional;
import java.util.Scanner;

public class AuthService {
    private final DataStore ds = DataStore.getInstance();

    public User login(Scanner sc) {
        System.out.print("Email: ");
        String email = sc.nextLine().trim();
        System.out.print("Password: ");
        String password = sc.nextLine().trim();

        Optional<User> user = ds.findUser(email);
        if (user.isPresent() && user.get().getPassword().equals(password)) {
            System.out.println("Welcome, " + user.get().getName() + "! Role: " + user.get().getRole());
            return user.get();
        }
        System.out.println("Invalid credentials.");
        return null;
    }

    public User signup(Scanner sc) {
        System.out.print("Name: ");
        String name = sc.nextLine().trim();
        System.out.print("Email: ");
        String email = sc.nextLine().trim();
        if (ds.findUser(email).isPresent()) { System.out.println("Email already registered."); return null; }
        System.out.print("Password: ");
        String password = sc.nextLine().trim();
        System.out.print("Role (ADMIN/BORROWER): ");
        String roleStr = sc.nextLine().trim().toUpperCase();
        User.Role role;
        try { role = User.Role.valueOf(roleStr); } catch (Exception e) { System.out.println("Invalid role."); return null; }

        User user = new User(email, password, name, role);
        ds.users.add(user);
        System.out.println("Registered successfully! Security Deposit: " + user.getSecurityDeposit());
        return user;
    }
}
