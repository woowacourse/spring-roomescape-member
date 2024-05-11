package roomescape.domain;

public interface TokenProvider {

    String createToken(String payload);

    String getPayload(String token);
}
