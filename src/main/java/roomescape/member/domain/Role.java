package roomescape.member.domain;

public enum Role {
    MEMBER, ADMIN;

    public boolean isEqual(Role role) {
        return this.equals(role);
    }
}
