package roomescape.auth.config;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import org.springframework.stereotype.Component;

@Component
public class PasswordEncryptor {
    private static final String ALGORITHM = "SHA-256";

    public String encrypt(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance(ALGORITHM);
            byte[] hashedBytes = md.digest(password.getBytes());
            return Base64.getEncoder().encodeToString(hashedBytes);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("해쉬 알고리즘을 찾을 수 없습니다." + ALGORITHM, e);
        }
    }
}
