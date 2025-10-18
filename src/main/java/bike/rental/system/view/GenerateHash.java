package bike.rental.system.view;

import org.mindrot.jbcrypt.BCrypt;

public class GenerateHash {
    public static void main(String[] args) {
        String password = "admin123";
        String salt = BCrypt.gensalt();
        String hash = BCrypt.hashpw(password, salt);
        System.out.println("New hash for admin123: " + hash);
    }
}