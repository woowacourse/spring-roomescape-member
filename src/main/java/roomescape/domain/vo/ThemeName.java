package roomescape.domain.vo;

import roomescape.exception.DomainViolationException;

public record ThemeName(
    String value
) {

    public ThemeName {
        validateNotBlank(value);
    }

    private void validateNotBlank(String value) {
        if (value == null || value.isBlank()) {
            throw new DomainViolationException("빈 문자열은 테마명으로 사용할 수 없습니다.");
        }
    }
}
