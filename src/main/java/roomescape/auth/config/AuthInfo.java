package roomescape.auth.config;

public class AuthInfo {
    private final Long memberId;
    private final String name;

    public AuthInfo(final Long memberId, final String name) {
        this.memberId = memberId;
        this.name = name;
    }

    public Long getMemberId() {
        return memberId;
    }

    public String getName() {
        return name;
    }
}
