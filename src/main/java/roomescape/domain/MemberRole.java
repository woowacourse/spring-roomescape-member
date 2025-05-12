package roomescape.domain;

import roomescape.exception.ForbiddenAuthorityException;

public enum MemberRole {

    ADMIN,
    USER;

    public void validateAdmin() {
        if (this == ADMIN) {
            return;
        }
        throw new ForbiddenAuthorityException("관리자가 아닙니다.");
    }
}
