package roomescape.domain;

import roomescape.exception.ErrorType;
import roomescape.exception.InvalidClientRequestException;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

public class Password {
    private final String hashValue;
    private final String salt;

    public Password(final String password) {
        validateEmpty("password", password);
        try {
            this.salt = generateSalt();
            this.hashValue = hashPassword(password, salt);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public Password(String password, String salt) {
        try {
            this.salt = salt;
            this.hashValue = hashPassword(password, salt);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    private String generateSalt() throws NoSuchAlgorithmException {
        byte[] salt = new byte[16];
        SecureRandom sr = SecureRandom.getInstanceStrong();
        sr.nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }

    private String hashPassword(String password, String salt) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(Base64.getDecoder().decode(salt));
        byte[] hashedPassword = md.digest(password.getBytes());
        return Base64.getEncoder().encodeToString(hashedPassword);
    }

    private void validateEmpty(final String fieldName, final String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new InvalidClientRequestException(ErrorType.EMPTY_VALUE_NOT_ALLOWED, fieldName, "");
        }
    }

    public String getHashValue() {
        return hashValue;
    }

    public String getSalt() {
        return salt;
    }

    public boolean isCorrect(String hashValue) {
        return this.hashValue.equals(hashValue);
    }
}
