package roomescape.member.application;

public interface TokenProvider {

    String createToken(String payload);
    String getPayload(String token);
}
