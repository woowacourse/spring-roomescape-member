package roomescape.exception;

public class RoleValidator {

    private RoleValidator() {
    }

    public static void requireAdmin(String role) {
        if (!"admin".equals(role)) {
            throw new RoomEscapeException(ErrorCode.FORBIDDEN);
        }
    }
}
