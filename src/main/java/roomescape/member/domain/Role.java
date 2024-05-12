package roomescape.member.domain;

public enum Role {
    ADMIN, GUEST;

    public boolean isAdmin() {
        return this == ADMIN;
    }
}
