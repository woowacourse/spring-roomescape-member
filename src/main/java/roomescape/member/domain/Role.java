package roomescape.member.domain;

public enum Role {

    MEMBER,
    ADMIN,
    ;

    public boolean isAdmin() {
        return this == ADMIN;
    }
}
