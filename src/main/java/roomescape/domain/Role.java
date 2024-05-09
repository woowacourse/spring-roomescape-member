package roomescape.domain;

public enum Role {
    MEMBER,
    ADMIN;

    public static Role from(final String name) {
        return valueOf(name);
    }
}
