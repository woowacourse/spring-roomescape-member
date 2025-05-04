package roomescape.dto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import roomescape.common.exception.InvalidRequestException;
import roomescape.dto.request.ReservationTimeCreateRequest;

import java.time.LocalTime;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class ReservationTimeCreatedRequestValidationTest {

    @Test
    @DisplayName("ReservationTimeCreateRequest 생성 테스트")
    void generateReservationTimeCreateRequest() {
        assertThatCode(() -> new ReservationTimeCreateRequest(LocalTime.of(12, 30)))
                .doesNotThrowAnyException();
    }

    @ParameterizedTest
    @MethodSource("getInvalidReservationTimeCreateRequest")
    @DisplayName("ReservationTimeCreateRequest 필수 값 검증 테스트")
    void invalidReservationTimeCreateRequest(Supplier<ReservationTimeCreateRequest> supplier) {
        assertThatThrownBy(supplier::get)
                .isInstanceOf(InvalidRequestException.class);
    }

    static Stream<Arguments> getInvalidReservationTimeCreateRequest() {
        return Stream.of(
                Arguments.arguments((Supplier<ReservationTimeCreateRequest>) () -> new ReservationTimeCreateRequest(null))
        );
    }
}
