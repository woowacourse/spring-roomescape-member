package roomescape.application.util;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordUtil {

    public static String encrypt(String rawPassword) {
        return BCrypt.hashpw(rawPassword, BCrypt.gensalt());
    }

    public static boolean matches(String rawPassword, String encodedPassword) {
        return BCrypt.checkpw(rawPassword, encodedPassword);
    }
}
