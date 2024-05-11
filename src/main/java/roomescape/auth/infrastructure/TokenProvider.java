package roomescape.auth.infrastructure;

public interface TokenProvider {

    String createToken(String payload);

    String getPayload(String token);

    boolean isTokenTimeOut(String token);
}
