package roomescape.user;

public interface TokenProvider {

    String createToken(String payload);

    String parsePayload(String token);
}
