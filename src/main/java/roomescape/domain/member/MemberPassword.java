package roomescape.domain.member;

import roomescape.domain.exception.InvalidValueException;

public class MemberPassword {

    private final String value;

    public MemberPassword(String value) {
        validateNotNullOrEmpty(value);
        this.value = value;
    }

    private void validateNotNullOrEmpty(String value) {
        if (value == null || value.isBlank()) {
            throw new InvalidValueException("비밀번호을 입력해주세요.");
        }
    }

    public boolean isNotSame(MemberPassword other) {
        return !this.value.equals(other.value);
    }

    public String getValue() {
        return value;
    }
}
