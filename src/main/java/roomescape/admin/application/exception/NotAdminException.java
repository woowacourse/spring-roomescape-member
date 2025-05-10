package roomescape.admin.application.exception;

public class NotAdminException extends RuntimeException {
    public NotAdminException(String message) {
        super(message);
    }
}
