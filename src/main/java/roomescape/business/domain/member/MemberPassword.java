package roomescape.business.domain.member;

import roomescape.exception.MemberException;

public record MemberPassword(
        String value
) {

    public MemberPassword {
        if (value == null || value.isBlank()) {
            throw new MemberException("비밀번호는 null이거나 빈 문자열일 수 없습니다.");
        }
    }
}
