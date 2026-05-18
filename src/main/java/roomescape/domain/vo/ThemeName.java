package roomescape.domain.vo;

import roomescape.exception.BusinessException;
import roomescape.exception.ErrorCode;

public record ThemeName(
        String value
) {

    public ThemeName {
        validateNotBlank(value);
    }

    public static ThemeName from(String name) {
        return new ThemeName(name);
    }

    private void validateNotBlank(String value) {
        if (value == null || value.isBlank()) {
            throw new BusinessException(ErrorCode.BLANK_INPUT, "빈 문자열은 테마 이름으로 사용할 수 없습니다.");
        }
    }
}
