package roomescape.domain;

public interface AuthenticationTokenProvider {

    String createToken(AuthenticationInfo authenticationInfo);

    long getIdentifier(String token);

    AuthenticationInfo getPayload(String token);

    boolean isValidToken(String token);
}
