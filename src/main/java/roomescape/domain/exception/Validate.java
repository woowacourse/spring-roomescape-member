package roomescape.domain.exception;

public class Validate {
    public void AllNonNull(Object... objects) {
        for (Object object : objects) {
            requireNonNull(object);
        }
    }

    private <T> void requireNonNull(T obj) {
        if (obj == null)
            throw new IllegalNullArgumentException();
    }

}
