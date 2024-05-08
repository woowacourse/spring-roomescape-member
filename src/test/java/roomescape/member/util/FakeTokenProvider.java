package roomescape.member.util;

public class FakeTokenProvider implements TokenProvider{
    @Override
    public String createToken(String payload) {
        return payload;
    }
}
