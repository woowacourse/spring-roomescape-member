package roomescape.member.domain;

public enum Role {
    ADMIN,
    MEMBER;

    public boolean isNotAdmin() {
        return this != ADMIN;
    }
}
