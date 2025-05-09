package roomescape.domain.enums;

public enum Role {
    ADMIN("ADMIN"),
    USER("USER");

    private String role;

    Role(String role) {
        this.role = role;
    }

    public static Role valueOfResultSet(String role) {
        return Role.valueOf(role);
    }
}
