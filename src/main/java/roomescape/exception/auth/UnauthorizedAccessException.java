package roomescape.exception.auth;

import roomescape.business.domain.Role;

public class UnauthorizedAccessException extends RuntimeException {

    public UnauthorizedAccessException() {
        super("권한이 없습니다.");
    }

    public UnauthorizedAccessException(final Role userRole, final Role requiredRole) {
        super("사용자의 권한이 해당 기능을 수행하기에 부족합니다. 현재 권한: " + userRole + ", 필요한 권한: " + requiredRole);
    }

    public UnauthorizedAccessException(final String message) {
        super(message);
    }
}
