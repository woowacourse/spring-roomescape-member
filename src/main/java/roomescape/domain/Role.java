package roomescape.domain;

import roomescape.exception.BadRequestException;

public enum Role {
    MEMBER, ADMIN;

    public static Role from(final String role) {
        if (MEMBER.name().equals(role)) {
            return MEMBER;
        }
        if (ADMIN.name().equals(role)) {
            return ADMIN;
        }
        throw new BadRequestException("일치하는 role이 없습니다.");
    }
}
