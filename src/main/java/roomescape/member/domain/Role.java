package roomescape.member.domain;

public enum Role {
    ADMIN,
    MEMBER;

    public boolean isMember() {
        return this == MEMBER;
    }
}
