package bike.rental.system.util;

import org.mindrot.jbcrypt.BCrypt;

public class GenerateHash {
    public static void main(String[] args) {
        String password = "customer123";
        String hashed = BCrypt.hashpw(password, BCrypt.gensalt());
        System.out.println("Hashed password for customer123: " + hashed);
    }
}