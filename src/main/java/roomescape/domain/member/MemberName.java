package roomescape.domain.member;

import roomescape.domain.exception.InvalidValueException;

public class MemberName {

    private final String value;

    public MemberName(String value) {
        validateNotNullOrEmpty(value);
        this.value = value;
    }

    private void validateNotNullOrEmpty(String value) {
        if (value == null || value.isBlank()) {
            throw new InvalidValueException("이름을 입력해주세요.");
        }
    }

    public String getValue() {
        return value;
    }
}
