package roomescape.common.auth;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PasswordEncryptor {

    private static final String ALGORITHM = "SHA-256";

    public static String encrypt(final String password) {
        try {
            final MessageDigest md = MessageDigest.getInstance(ALGORITHM);
            final byte[] bytes = password.getBytes();
            final byte[] digest = md.digest(bytes);

            final StringBuilder hex = new StringBuilder();

            for (final byte b : digest) {
                hex.append(String.format("%02x", b));
            }
            return hex.toString();

        } catch (final NoSuchAlgorithmException e) {
            throw new IllegalArgumentException("비밀번호 암호화에 실패했습니다.");
        }
    }

    public static boolean matches(final String password, final String rowPassword) {
        return encrypt(password).equals(rowPassword);
    }
}
