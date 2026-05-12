package roomescape.global;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.function.Supplier;
import roomescape.domain.global.exception.BaseException;
import roomescape.domain.global.exception.ErrorCode;

public final class ExceptionAssertions {

    private ExceptionAssertions() {
    }

    public static <E extends BaseException, T> void assertErrorCode(Supplier<T> supplier,
        Class<E> type, ErrorCode expected) {
        Throwable throwable = toThrowable(supplier);
        assertAll(
            () -> assertThat(throwable).isInstanceOf(type),
            () -> {
                E exception = type.cast(throwable);
                assertThat(exception.getErrorCode()).isEqualTo(expected);
            }
        );

    }

    public static <E extends BaseException, T> void assertErrorCode(Runnable runnable,
        Class<E> type, ErrorCode expected) {
        Throwable throwable = toThrowable(runnable);
        assertAll(
            () -> assertThat(throwable).isInstanceOf(type),
            () -> {
                E exception = type.cast(throwable);
                assertThat(exception.getErrorCode()).isEqualTo(expected);
            }
        );

    }

    private static <T> Throwable toThrowable(Supplier<T> supplier) {
        return catchThrowable(supplier::get);
    }

    private static Throwable toThrowable(Runnable runnable) {
        return catchThrowable(runnable::run);
    }
}
