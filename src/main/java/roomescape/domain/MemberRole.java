package roomescape.domain;

public enum MemberRole {

    NORMAL, ADMIN;

    public boolean isAdmin() {
        return this == ADMIN;
    }
}
