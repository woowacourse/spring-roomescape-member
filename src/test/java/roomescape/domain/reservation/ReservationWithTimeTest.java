package roomescape.domain.reservation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDate;
import java.time.LocalTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.domain.reservationTime.ReservationTime;
import roomescape.exception.InvalidRequestValueException;

class ReservationWithTimeTest {
    private ReservationTime defaultTime;
    private long defaultThemeId;

    @BeforeEach
    void setUp() {
        defaultTime = new ReservationTime(1L, LocalTime.of(10, 0));
        defaultThemeId = 1L;
    }

    @Test
    @DisplayName("정상 생성 테스트")
    void initTest() {
        long id = 1L;
        String name = "브라운";
        LocalDate date = LocalDate.now().plusDays(1);

        ReservationWithTime reservation = new ReservationWithTime(id, name, date, defaultTime, defaultThemeId);

        assertAll(
                () -> assertThat(reservation.id()).isEqualTo(id),
                () -> assertThat(reservation.name()).isEqualTo(name),
                () -> assertThat(reservation.date()).isEqualTo(date),
                () -> assertThat(reservation.reservationTime()).isEqualTo(defaultTime),
                () -> assertThat(reservation.themeId()).isEqualTo(defaultThemeId)
        );
    }

    @Test
    @DisplayName("수정 값이 기존 정보와 하나라도 다른 경우 검증 성공 테스트")
    void validateEqualValueSuccess() {
        LocalDate futureDate = LocalDate.now().plusDays(1);
        ReservationWithTime reservation = new ReservationWithTime(1L, "브라운", futureDate, defaultTime, defaultThemeId);

        assertThatCode(() -> reservation.validateEqualValue("테스트", futureDate, defaultTime.id(), defaultThemeId))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("수정하려는 정보가 기존 정보와 완전히 동일한 경우 예외 테스트")
    void validateEqualValueSameFail() {
        LocalDate futureDate = LocalDate.now().plusDays(1);
        ReservationWithTime reservation = new ReservationWithTime(1L, "브라운", futureDate, defaultTime, defaultThemeId);

        assertThatThrownBy(() -> reservation.validateEqualValue("브라운", futureDate, defaultTime.id(), defaultThemeId))
                .isInstanceOf(InvalidRequestValueException.class)
                .hasMessage("기존 정보와 동일하여 수정할 내용이 없습니다.");
    }

    @Test
    @DisplayName("현재 시점 기준 미래 예약 날짜에 대한 과거 여부 검증 성공 테스트")
    void validDateReservationPastDateTimeSuccess() {
        LocalDate futureDate = LocalDate.now().plusDays(1);
        ReservationWithTime futureReservation = new ReservationWithTime(1L, "브라운", futureDate, defaultTime, defaultThemeId);

        assertThatCode(() -> futureReservation.validDateReservationPastDateTime("이미 지난 예약은 수정할 수 없습니다."))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("이미 지난 시점의 예약 날짜일 경우 과거 여부 검증 예외 테스트")
    void validDateReservationPastDateTimeFail() {
        LocalDate pastDate = LocalDate.now().minusDays(1);
        ReservationWithTime pastReservation = new ReservationWithTime(1L, "브라운", pastDate, defaultTime, defaultThemeId);

        String errorMessage = "이미 지난 예약은 수정할 수 없습니다.";

        assertThatThrownBy(() -> pastReservation.validDateReservationPastDateTime(errorMessage))
                .isInstanceOf(InvalidRequestValueException.class)
                .hasMessage(errorMessage);
    }
}