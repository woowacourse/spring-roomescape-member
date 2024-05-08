package roomescape.config.security;

public enum JwtClaimKey {
    MEMBER_ID("memberId"),
    MEMBER_NAME("memberName"),
    ROLE("role"),
    ;

    private final String key;

    JwtClaimKey(final String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
