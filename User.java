public abstract class User {
    String email, password;

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public boolean login(String e, String p) {
        return email.equals(e) && password.equals(p);
    }

    public abstract void menu(Library lib);
}