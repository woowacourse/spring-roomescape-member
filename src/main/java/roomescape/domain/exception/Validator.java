package roomescape.domain.exception;

public class Validator {
    public static void allNonNull(Object... objects) {
        for (Object object : objects) {
            requireNonNull(object);
        }
    }

    private static <T> void requireNonNull(T obj) {
        if (obj == null)
            throw new IllegalNullArgumentException();
    }
}
