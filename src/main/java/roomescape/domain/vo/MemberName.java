package roomescape.domain.vo;

import roomescape.exception.BusinessException;
import roomescape.exception.ErrorCode;

public record MemberName(
        String value
) {
    public MemberName {
        validateNotBlank(value);
    }

    public static MemberName from(String name) {
        return new MemberName(name);
    }

    private void validateNotBlank(String value) {
        if (value == null || value.trim().isBlank()) {
            throw new BusinessException(ErrorCode.BLANK_INPUT, "빈 문자열은 이름으로 사용할 수 없습니다.");
        }
    }
}
