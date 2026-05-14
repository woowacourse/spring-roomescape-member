package roomescape.domain.global.handler;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import roomescape.domain.global.exception.custom.BadRequestException;
import roomescape.domain.global.exception.error.ErrorCode;

class ThrowableFinderTest {

    @Nested
    @DisplayName("find 테스트")
    class FindTest {

        @Test
        @DisplayName("전달받은 예외가 찾는 타입이면 해당 예외를 반환한다.")
        void 성공1() {
            BadRequestException exception = new BadRequestException(
                ErrorCode.COMMON_INVALID_REQUEST_BODY, List.of());

            Optional<BadRequestException> actual = ThrowableFinder.find(
                exception, BadRequestException.class);

            assertThat(actual).containsSame(exception);
        }

        @Test
        @DisplayName("cause chain 안에 찾는 타입이 있으면 해당 예외를 반환한다.")
        void 성공2() {
            BadRequestException badRequestException = new BadRequestException(
                ErrorCode.COMMON_INVALID_REQUEST_BODY, List.of());
            RuntimeException exception = new RuntimeException(
                new IllegalStateException(badRequestException));

            Optional<BadRequestException> actual = ThrowableFinder.find(
                exception, BadRequestException.class);

            assertThat(actual).containsSame(badRequestException);
        }

        @Test
        @DisplayName("cause chain 안에 찾는 타입이 없으면 빈 Optional을 반환한다.")
        void 성공3() {
            RuntimeException exception = new RuntimeException(new IllegalStateException());

            Optional<BadRequestException> actual = ThrowableFinder.find(
                exception, BadRequestException.class);

            assertThat(actual).isEmpty();
        }

        @Test
        @DisplayName("전달받은 예외가 null이면 빈 Optional을 반환한다.")
        void 성공4() {
            Optional<BadRequestException> actual = ThrowableFinder.find(
                null, BadRequestException.class);

            assertThat(actual).isEmpty();
        }
    }
}
