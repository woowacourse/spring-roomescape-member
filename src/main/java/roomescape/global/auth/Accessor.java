package roomescape.global.auth;

public record Accessor(
        String role
) {
    public boolean isAdmin() {
        return "ADMIN".equals(role);
    }
}
