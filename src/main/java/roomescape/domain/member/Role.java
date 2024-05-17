package roomescape.domain.member;

public enum Role {
    ADMIN,
    MEMBER,
    GUEST;

    public static Role from(String name) {
        try {
            return Role.valueOf(name.toUpperCase());
        } catch (IllegalArgumentException e) {
            return GUEST;
        }
    }

    public boolean isMember() {
        return this == MEMBER;
    }

    public boolean isAdmin() {
        return this == ADMIN;
    }
}
