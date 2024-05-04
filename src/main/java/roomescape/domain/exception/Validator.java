package roomescape.domain.exception;

public class Validator {
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
}
