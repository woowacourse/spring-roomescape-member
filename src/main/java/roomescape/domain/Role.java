package roomescape.domain;

public enum Role {
    MEMBER("MEMBER"),
    ADMIN("ADMIN");

    private final String role;

    Role(String role) {
        this.role = role;
    }

    public String role() {
        return role;
    }

    public boolean isAdmin() {
        return this == ADMIN;
    }
}
