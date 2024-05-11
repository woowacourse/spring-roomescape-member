package roomescape.domain.member;

public enum Role {
    ADMIN, MEMBER;

    boolean isAdmin() {
        return this == ADMIN;
    }
}
