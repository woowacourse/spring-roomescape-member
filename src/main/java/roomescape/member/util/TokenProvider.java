package roomescape.member.util;

public interface TokenProvider {
    String createToken(String payload);

    public String getPayload(String token);
}
