package roomescape.dto;

import roomescape.exception.CustomException;

import static roomescape.exception.CustomExceptionCode.*;

public class InputValidator {

    public static void validateNotNull(Object... inputs) {
        for (Object input : inputs) {
            if (input == null) {
                throw new CustomException(NULL_INPUT_NOT_ALLOWED);
            }
        }
    }

    public static void validateNotEmpty(String... inputs) {
        for (String input : inputs) {
            if (input.isEmpty()) {
                throw new CustomException(BLANK_INPUT_NOT_ALLOWED);
            }
        }
    }
}
