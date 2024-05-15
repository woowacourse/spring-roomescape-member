package roomescape.member.domain;

public enum Role {
    ADMIN,
    MEMBER;

    public boolean isAdmin() {
        return this == ADMIN;
    }
}
