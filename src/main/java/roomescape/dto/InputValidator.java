package roomescape.dto;

import roomescape.dto.exception.InputNotAllowedException;

class InputValidator {

    static void validateNotNull(Object... inputs) {
        for (Object input : inputs) {
            if (input == null) {
                throw new InputNotAllowedException("null은 입력할 수 없습니다.");
            }
        }
    }

    static void validateNotEmpty(String... inputs) {
        for (String input : inputs) {
            if (input.isEmpty()) {
                throw new InputNotAllowedException("빈 문자열은 입력할 수 없습니다.");
            }
        }
    }
}
