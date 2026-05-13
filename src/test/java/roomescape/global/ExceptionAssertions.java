package roomescape.global;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import java.util.function.Supplier;
import roomescape.domain.global.exception.BadRequestException;
import roomescape.domain.global.exception.BaseException;
import roomescape.domain.global.exception.ErrorCode;
import roomescape.domain.global.exception.ErrorDetail;

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

    public static <T> void assertErrorCodeWithErrors(Runnable runnable, ErrorCode expectedErrorCode, List<ErrorDetail> expectedErrorDetails) {
        Throwable throwable = toThrowable(runnable);
        assertThat(throwable).isInstanceOf(BadRequestException.class);
        BadRequestException exception = BadRequestException.class.cast(throwable);

        assertAll(
            () -> assertThat(exception.getErrorCode()).isEqualTo(expectedErrorCode),
            () -> assertThat(exception.getErrors())
                .containsExactlyInAnyOrderElementsOf(expectedErrorDetails)
        );
    }

    private static <T> Throwable toThrowable(Supplier<T> supplier) {
        return catchThrowable(supplier::get);
    }

    private static Throwable toThrowable(Runnable runnable) {
        return catchThrowable(runnable::run);
    }
}
