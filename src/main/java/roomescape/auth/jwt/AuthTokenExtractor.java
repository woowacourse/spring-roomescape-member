package roomescape.auth.jwt;

public interface AuthTokenExtractor {

    String extractPayload(String token);

    String extractRole(String token);
}
