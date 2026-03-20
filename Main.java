public class Main {
    public static void main(String[] args) {
        Library lib = new Library();

        while (true) {
            System.out.println("\n===== LIBRARY SYSTEM =====");

            User user = lib.authenticate();

            if (user != null) {
                user.menu(lib);
            } else {
                System.out.println("Invalid login!");
            }
        }
    }
}