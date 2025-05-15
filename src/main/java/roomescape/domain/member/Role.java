package roomescape.domain.member;

public enum Role {
    MEMBER,
    ADMIN;

    public static Role getDefaultRole() {
        return MEMBER;
    }
}
