package roomescape.application;

public interface AuthenticationTokenProvider {

    String createToken(String payload);

    String getPayload(String token);

    boolean isValidToken(String token);
}
