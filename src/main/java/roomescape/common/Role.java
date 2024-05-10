package roomescape.common;

public enum Role {
    MEMBER,
    ADMIN,
    ;

    public static boolean isAdmin(String role) {
        return ADMIN.name().equals(role);
    }
}
