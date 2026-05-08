package roomescape.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.exception.InvalidRequestException;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ReservationTest {

    private final ReservationTime time = new ReservationTime(1L, LocalTime.of(10, 0));
    private final Theme theme = new Theme(1L, "레벨2 탈출", "우테코 레벨2를 탈출하는 내용입니다.", "https://example.com/theme.png");

    @Test
    @DisplayName("예약자 이름이 비어있으면 도메인 예외가 발생한다.")
    void create_fail_whenNameIsBlank() {
        assertInvalidRequestException(
                () -> new Reservation(" ", LocalDate.of(2023, 8, 5), time, theme),
                "예약자 이름은 비어 있을 수 없습니다."
        );
    }

    @Test
    @DisplayName("예약 날짜가 null이면 도메인 예외가 발생한다.")
    void create_fail_whenDateIsNull() {
        assertInvalidRequestException(
                () -> new Reservation("브라운", null, time, theme),
                "예약 날짜는 비어 있을 수 없습니다."
        );
    }

    @Test
    @DisplayName("예약 시간이 null이면 도메인 예외가 발생한다.")
    void create_fail_whenTimeIsNull() {
        assertInvalidRequestException(
                () -> new Reservation("브라운", LocalDate.of(2023, 8, 5), null, theme),
                "예약 시간은 비어 있을 수 없습니다."
        );
    }

    @Test
    @DisplayName("예약 테마가 null이면 도메인 예외가 발생한다.")
    void create_fail_whenThemeIsNull() {
        assertInvalidRequestException(
                () -> new Reservation("브라운", LocalDate.of(2023, 8, 5), time, null),
                "테마 정보는 비어 있을 수 없습니다."
        );
    }

    @Test
    @DisplayName("예약 id가 null이면 도메인 예외가 발생한다.")
    void withId_fail_whenIdIsNull() {
        Reservation reservation = new Reservation("브라운", LocalDate.of(2023, 8, 5), time, theme);

        assertInvalidRequestException(
                () -> reservation.withId(null),
                "예약 id는 비어 있을 수 없습니다."
        );
    }

    @Test
    @DisplayName("이미 id가 있는 예약에 id를 부여하면 도메인 예외가 발생한다.")
    void withId_fail_whenReservationAlreadyHasId() {
        Reservation reservation = new Reservation(1L, "브라운", LocalDate.of(2023, 8, 5), time, theme);

        assertInvalidRequestException(
                () -> reservation.withId(2L),
                "이미 식별자가 존재하는 예약입니다."
        );
    }

    private void assertInvalidRequestException(Runnable runnable, String message) {
        assertThatThrownBy(runnable::run)
                .isInstanceOf(InvalidRequestException.class)
                .hasMessage(message);
    }
}
