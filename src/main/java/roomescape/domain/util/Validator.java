package roomescape.domain.util;

import roomescape.domain.exception.IllegalNullArgumentException;

public class Validator {
    private Validator() {
    }

    public static void nonNull(Object... objects) {
        for (Object object : objects) {
            validateNonNull(object);
        }
    }

    private static <T> void validateNonNull(T obj) {
        if (obj == null)
            throw new IllegalNullArgumentException();
    }

    public static void notEmpty(String... values) {
        for (String value : values) {
            validateNotEmpty(value);
        }
    }

    private static void validateNotEmpty(String value) {
        if (value.isEmpty()) {
            throw new IllegalArgumentException("비어있는 값이 존재합니다.");
        }
    }

    public static void overSize(int maxLength, String... values) {
        for (String value : values) {
            validateOverSize(maxLength, value);
        }
    }

    private static void validateOverSize(int maxLength, String value) {
        if (value.length() > maxLength) {
            throw new IllegalArgumentException("문자열(%s) 최대 길이인 %d를 초과했습니다.".formatted(value, maxLength));
        }
    }
}
