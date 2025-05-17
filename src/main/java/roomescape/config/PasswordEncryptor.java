package roomescape.config;

import java.security.spec.KeySpec;
import java.util.Base64;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public final class PasswordEncryptor {

    private static final String ALGORITHM = "PBKDF2WithHmacSHA1";
    private static final int ITERATIONS = 65536;
    private static final int KEY_LENGTH = 128;
    private static final byte[] SALT = "afweawgjknajk3ngkjanw3kgawaagsd".getBytes();

    public static String encrypt(String rawPassword) {
        try {
            KeySpec spec = new PBEKeySpec(rawPassword.toCharArray(), SALT, ITERATIONS, KEY_LENGTH);
            SecretKeyFactory factory = SecretKeyFactory.getInstance(ALGORITHM);
            byte[] hashed = factory.generateSecret(spec).getEncoded();
            return Base64.getEncoder().encodeToString(hashed);
        } catch (Exception e) {
            throw new IllegalStateException("비밀번호 암호화에 실패했습니다.", e);
        }
    }

    public static boolean matches(String rawPassword, String value) {
        try {
            KeySpec spec = new PBEKeySpec(rawPassword.toCharArray(), SALT, ITERATIONS, KEY_LENGTH);
            SecretKeyFactory factory = SecretKeyFactory.getInstance(ALGORITHM);
            byte[] hashed = factory.generateSecret(spec).getEncoded();
            String hashedPassword = Base64.getEncoder().encodeToString(hashed);
            return hashedPassword.equals(value);
        } catch (Exception e) {
            throw new IllegalStateException("비밀번호 비교에 실패했습니다.", e);
        }
    }
}
