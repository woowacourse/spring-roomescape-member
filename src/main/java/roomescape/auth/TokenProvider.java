package roomescape.auth;

public interface TokenProvider {
    String createToken(String subject);

    String extractSubject(String token);
}
