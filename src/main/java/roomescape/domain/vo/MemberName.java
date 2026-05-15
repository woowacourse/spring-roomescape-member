package roomescape.domain.vo;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public record MemberName(
        @JsonValue String value
) {
    @JsonCreator
    public MemberName {
        validateNotBlank(value);
    }

    private void validateNotBlank(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("빈 문자열은 이름으로 사용할 수 없습니다.");
        }
    }
}
