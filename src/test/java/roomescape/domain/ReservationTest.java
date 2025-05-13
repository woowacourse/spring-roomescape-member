package roomescape.domain;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

@DisplayName("예약(Reservation) 테스트")
class ReservationTest {

    @ParameterizedTest
    @DisplayName("날짜 또는 시간이 과거인 경우 예외가 발생한다")
    @MethodSource("createPastDateTimeParameters")
    void validatePastDateTimeThrowsException(LocalDate date, LocalTime time) {
        //given
        Reservation testReservation = createTestReservationByDateAndTime(date, time);

        //when && then
        assertThatThrownBy(testReservation::validatePastDateTime)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("지난 날짜와 시간의 예약은 생성 불가능합니다.");
    }

    private static Stream<Arguments> createPastDateTimeParameters() {
        LocalDateTime now = LocalDateTime.now();
        return Stream.of(
                Arguments.of(now.toLocalDate().minusDays(1), now.toLocalTime()),
                Arguments.of(now.toLocalDate(), now.toLocalTime().minusMinutes(1))
        );
    }

    @ParameterizedTest
    @DisplayName("날짜 또는 시간이 미래 혹은 같은 경우 예외가 발생하지 않는다.")
    @MethodSource("futureDateTimeParameters")
    void validatePastDateTimeSuccessWithFuture(LocalDate date, LocalTime time) {
        // given
        Reservation testReservation = createTestReservationByDateAndTime(date, time);

        //when && then
        assertThatNoException()
                .isThrownBy(testReservation::validatePastDateTime);
    }

    private static Stream<Arguments> futureDateTimeParameters() {
        LocalDateTime now = LocalDateTime.now();
        return Stream.of(
                Arguments.of(now.toLocalDate(), now.toLocalTime()),
                Arguments.of(now.toLocalDate(), now.toLocalTime().plusMinutes(1)),
                Arguments.of(now.toLocalDate().plusDays(1), now.toLocalTime())
        );
    }

    private Reservation createTestReservationByDateAndTime(LocalDate date, LocalTime time) {
        return new Reservation(
                1L,
                date,
                new ReservationTime(time),
                new Theme("moda", "description", "thumbnail"),
                new Member("이름", "test@email.com", "password", Role.ADMIN)
        );
    }
}
