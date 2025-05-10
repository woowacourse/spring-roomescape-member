package roomescape.business.model.vo;

import roomescape.exception.business.InvalidCreateArgumentException;

public record UserName(
        String value
) {
    private static final int MAX_LENGTH = 10;

    public UserName {
        validateMaxLength(value);
        validateNameDoesNotContainsNumber(value);
    }

    private static void validateMaxLength(final String name) {
        if (name.length() > MAX_LENGTH) {
            throw new InvalidCreateArgumentException("이름은 %d자를 넘길 수 없습니다.".formatted(MAX_LENGTH));
        }
    }

    private static void validateNameDoesNotContainsNumber(final String name) {
        for (char c : name.toCharArray()) {
            if (Character.isDigit(c)) {
                throw new InvalidCreateArgumentException("이름에 숫자는 포함될 수 없습니다.");
            }
        }
    }
}
