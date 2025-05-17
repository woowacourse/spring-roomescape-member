package roomescape.member.domain;

public enum MemberRole {
    MEMBER,
    ADMIN,
    ;

    public boolean isAdmin() {
        return this == ADMIN;
    }

    public static MemberRole from(String role) {
        try {
            return MemberRole.valueOf(role.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("멤버 역할로 변활할 수 없습니다.  role=" + role);
        }
    }
}
