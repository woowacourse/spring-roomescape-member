package roomescape.domain.global.handler;

import java.util.Optional;

public final class ThrowableFinder {

    private ThrowableFinder() {}

    static <E extends Throwable> Optional<E> find(Throwable throwable, Class<E> exceptionClass) {
        while (throwable != null) {
            if (exceptionClass.isInstance(throwable)) {
                return Optional.of(exceptionClass.cast(throwable));
            }
            throwable = throwable.getCause();
        }

        return Optional.empty();
    }
}
