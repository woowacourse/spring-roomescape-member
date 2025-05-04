package roomescape.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import roomescape.common.exception.InvalidRequestException;
import roomescape.dto.request.ReservationCreateRequest;

import java.time.LocalDate;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class ReservationCreatedRequestValidationTest {

    @Test
    @DisplayName("ReservationCreateRequest 정상 생성 테스트")
    void generateReservationCreateRequest() {
        assertThatCode(() -> new ReservationCreateRequest("홍길동", LocalDate.now(), 1L, 1L))
                .doesNotThrowAnyException();
    }

    @ParameterizedTest
    @MethodSource("getInvalidReservationCreateRequests")
    @DisplayName("ReservationCreateRequest 필수 값 검증 테스트")
    void invalidReservationCreateRequest(Supplier<ReservationCreateRequest> supplier) {
        assertThatThrownBy(supplier::get)
                .isInstanceOf(InvalidRequestException.class);
    }

    static Stream<Arguments> getInvalidReservationCreateRequests() {
        return Stream.of(
                Arguments.arguments((Supplier<ReservationCreateRequest>) () -> new ReservationCreateRequest(null, LocalDate.now(), 1L, 1L)),
                Arguments.arguments((Supplier<ReservationCreateRequest>) () -> new ReservationCreateRequest(" ", LocalDate.now(), 1L, 1L)),
                Arguments.arguments((Supplier<ReservationCreateRequest>) () -> new ReservationCreateRequest("홍길동", null, 1L, 1L)),
                Arguments.arguments((Supplier<ReservationCreateRequest>) () -> new ReservationCreateRequest("홍길동", LocalDate.now(), null, 1L)),
                Arguments.arguments((Supplier<ReservationCreateRequest>) () -> new ReservationCreateRequest("홍길동", LocalDate.now(), 1L, null))
        );
    }
}
