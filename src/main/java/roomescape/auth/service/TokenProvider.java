package roomescape.auth.service;

public interface TokenProvider {
    String createAccessToken(String payload);

    String getPayload(String token);

    boolean validateToken(String token);
}
