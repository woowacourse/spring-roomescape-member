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

    public Password(String rawPassword) {
        validateNonBlank(rawPassword);
        validateLength(rawPassword);
        this.encryptedPassword = encrypt(rawPassword);
    }

    private void validateNonBlank(String rawPassword) {
        if (rawPassword == null || rawPassword.isBlank()) {
            throw new IllegalArgumentException("비밀번호는 필수 입력값 입니다.");
        }
    }

    private void validateLength(String rawPassword) {
        if (rawPassword.length() < PASSWORD_MIN_LENGTH || rawPassword.length() > PASSWORD_MAX_LENGTH) {
            throw new IllegalArgumentException("비밀번호는 8자 이상 20자 이하여야 합니다.");
        }
    }

    public boolean matches(String other) {
        return encrypt(other).equals(encryptedPassword);
    }

    private String encrypt(String rawPassword) {
        String saltedPassword = rawPassword + SALT;
        md.update(saltedPassword.getBytes());
        byte[] byteData = md.digest();
        StringBuilder sb = new StringBuilder();
        for (byte b : byteData) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    public String getEncryptedPassword() {
        return encryptedPassword;
    }
}
