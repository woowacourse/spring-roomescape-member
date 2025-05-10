package roomescape.domain.auth.service;

public interface PasswordEncryptor {
    String encrypt(String rawPassword);
}
