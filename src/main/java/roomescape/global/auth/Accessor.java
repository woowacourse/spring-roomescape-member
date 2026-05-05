package roomescape.global.auth;

public record Accessor(
        String role
) {
    public boolean isAdmin() {
        return "ADMIN".equals(role);
    }

    public void validateAdmin() {
        if (!isAdmin()) {
            throw new ForbiddenException("관리자 권한이 필요한 기능입니다.");
        }
    }
}
