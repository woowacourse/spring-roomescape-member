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
}
