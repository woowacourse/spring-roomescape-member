package roomescape.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import roomescape.common.exception.DomainException;
import roomescape.common.exception.ErrorCode;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ReservationTest {

    private final ReservationTime time = new ReservationTime(1L, LocalTime.of(10, 0));
    private final Theme theme = new Theme(1L, "레벨2 탈출", "우테코 레벨2를 탈출하는 내용입니다.", "https://example.com/theme.png");

    @Test
    @DisplayName("예약자 이름이 비어있으면 도메인 예외가 발생한다.")
    void create_fail_when_name_is_blank() {
        assertDomainException(
                () -> new Reservation(" ", LocalDate.of(2023, 8, 5), time, theme),
                ErrorCode.INVALID_RESERVATION_NAME
        );
    }

    @Test
    @DisplayName("예약 날짜가 null이면 도메인 예외가 발생한다.")
    void create_fail_when_date_is_null() {
        assertDomainException(
                () -> new Reservation("브라운", null, time, theme),
                ErrorCode.INVALID_RESERVATION_DATE
        );
    }

    @Test
    @DisplayName("예약 시간이 null이면 도메인 예외가 발생한다.")
    void create_fail_when_time_is_null() {
        assertDomainException(
                () -> new Reservation("브라운", LocalDate.of(2023, 8, 5), null, theme),
                ErrorCode.INVALID_RESERVATION_TIME
        );
    }

    @Test
    @DisplayName("예약 테마가 null이면 도메인 예외가 발생한다.")
    void create_fail_when_theme_is_null() {
        assertDomainException(
                () -> new Reservation("브라운", LocalDate.of(2023, 8, 5), time, null),
                ErrorCode.INVALID_THEME
        );
    }

    @Test
    @DisplayName("예약 id가 null이면 도메인 예외가 발생한다.")
    void withId_fail_when_id_is_null() {
        Reservation reservation = new Reservation("브라운", LocalDate.of(2023, 8, 5), time, theme);

        assertDomainException(
                () -> reservation.withId(null),
                ErrorCode.INVALID_RESERVATION_ID
        );
    }

    @Test
    @DisplayName("이미 id가 있는 예약에 id를 부여하면 도메인 예외가 발생한다.")
    void withId_fail_when_reservation_already_has_id() {
        Reservation reservation = new Reservation(1L, "브라운", LocalDate.of(2023, 8, 5), time, theme);

        assertDomainException(
                () -> reservation.withId(2L),
                ErrorCode.RESERVATION_ALREADY_HAS_ID
        );
    }

    @ParameterizedTest
    @CsvSource(value = {
            "2025-05-11, true",
            "2027-05-11, false",
    })
    @DisplayName("예약의 날짜 및 시간이 이미 지났는지 여부를 반환한다.")
    public void isPast(LocalDate date, boolean expected) {
        // given
        Reservation reservation = new Reservation(1L, "브라운", date, time, theme);
        LocalDateTime now = LocalDateTime.of(
                LocalDate.of(2026, 5, 11), time.getStartAt());

        // when
        boolean result = reservation.isPast(now);

        // then
        assertThat(result).isEqualTo(expected);
    }

    private void assertDomainException(Runnable runnable, ErrorCode errorCode) {
        assertThatThrownBy(runnable::run)
                .isInstanceOfSatisfying(DomainException.class, exception ->
                        assertThat(exception.getErrorCode()).isEqualTo(errorCode)
                )
                .hasMessage(errorCode.message());
    }
}
