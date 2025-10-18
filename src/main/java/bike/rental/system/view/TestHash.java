package bike.rental.system.view; // Match an existing package or create a new one

import org.mindrot.jbcrypt.BCrypt;

public class TestHash {
    public static void main(String[] args) {
        String hash = "$2a$10$Y8xNlRnZ/NO4j3u6OjRYF.AvS8qknUd1GiwRp4i19RsRrxvBKmuDq"; // New hash
        System.out.println("Match: " + BCrypt.checkpw("admin123", hash));}
}