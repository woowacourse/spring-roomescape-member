package roomescape.business.model.vo;

import roomescape.exception.business.InvalidCreateArgumentException;

import static roomescape.exception.ErrorCode.USER_NAME_CONTAINS_NUMBER;
import static roomescape.exception.ErrorCode.USER_NAME_LENGTH_TOO_LONG;

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
            throw new InvalidCreateArgumentException(USER_NAME_LENGTH_TOO_LONG, MAX_LENGTH);
        }
    }

    private static void validateNameDoesNotContainsNumber(final String name) {
        for (char c : name.toCharArray()) {
            if (Character.isDigit(c)) {
                throw new InvalidCreateArgumentException(USER_NAME_CONTAINS_NUMBER);
            }
        }
    }
}
