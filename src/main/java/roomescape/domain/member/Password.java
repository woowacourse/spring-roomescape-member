package roomescape.domain.member;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Password {
    private static final MessageDigest md;
    private static final String SALT = "5RLs8HjD8L";
    private static final int PASSWORD_MIN_LENGTH = 8;
    private static final int PASSWORD_MAX_LENGTH = 20;

    static {
        try {
            md = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private final String encryptedPassword;

    public Password(String encryptedPassword) {
        this.encryptedPassword = encryptedPassword;
    }

    public static Password encryptFrom(String rawPassword) {
        validateNonBlank(rawPassword);
        validateLength(rawPassword);
        String encryptedPassword = encrypt(rawPassword);
        return new Password(encryptedPassword);
    }

    private static void validateNonBlank(String rawPassword) {
        if (rawPassword == null || rawPassword.isBlank()) {
            throw new IllegalArgumentException("비밀번호는 필수 입력값 입니다.");
        }
    }

    private static void validateLength(String rawPassword) {
        if (rawPassword.length() < PASSWORD_MIN_LENGTH || rawPassword.length() > PASSWORD_MAX_LENGTH) {
            throw new IllegalArgumentException("비밀번호는 8자 이상 20자 이하여야 합니다.");
        }
    }

    private static String encrypt(String rawPassword) {
        String saltedPassword = rawPassword + SALT;
        md.update(saltedPassword.getBytes());
        byte[] byteData = md.digest();
        StringBuilder sb = new StringBuilder();
        for (byte b : byteData) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    public boolean matches(String other) {
        return encryptedPassword.equals(encrypt(other));
    }

    public String getEncryptedPassword() {
        return encryptedPassword;
    }
}
