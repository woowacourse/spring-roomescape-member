package roomescape.infrastructure;

public interface TokenProvider {

    String createToken(String payload);

    String getPayload(String token);

    boolean isValidToken(String token);
}
