package roomescape.user.service;

public interface TokenProvider {

    String createToken(String payload);

    String parsePayload(String token);
}
