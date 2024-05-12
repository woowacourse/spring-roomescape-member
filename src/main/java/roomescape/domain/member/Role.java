package roomescape.domain.member;

public enum Role {
    ADMIN, MEMBER;

    public boolean isAdmin() {
        return this == ADMIN;
    }
}
