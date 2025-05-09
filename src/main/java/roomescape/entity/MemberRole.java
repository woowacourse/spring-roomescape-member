package roomescape.entity;

public enum MemberRole {
    ADMIN(),
    USER(),
    ;

    public static MemberRole from(String role) {
        return MemberRole.valueOf(role);
    }
}
