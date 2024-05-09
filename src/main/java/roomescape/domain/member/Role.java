package roomescape.domain.member;

public enum Role {

    ADMIN("admin"),
    USER("user");

    private final String value;

    Role(String value) {
        this.value = value;
    }

    public static Role find(String role) {
        if (ADMIN.value.equals(role)) {
            return ADMIN;
        }
        return USER;
    }

    public String getValue() {
        return value;
    }
}
