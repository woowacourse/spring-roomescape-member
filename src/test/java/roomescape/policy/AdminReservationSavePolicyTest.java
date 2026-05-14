package roomescape.policy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThatCode;

class AdminReservationSavePolicyTest {

    private static final LocalDate FIXED_TODAY = LocalDate.of(2026, 5, 8);
    private static final LocalDateTime FIXED_NOW = LocalDateTime.of(FIXED_TODAY, LocalTime.of(12, 0));
    private static final ReservationTime PAST_TIME = new ReservationTime(1L, LocalTime.of(9, 0));
    private static final Theme THEME = new Theme(1L, "우주 정거장", "설명", "https://example.com/1.jpg");

    private AdminReservationSavePolicy policy;

    @BeforeEach
    void setUp() {
        policy = new AdminReservationSavePolicy();
    }

    @Test
    void 지난_날짜도_예약할_수_있다() {
        Reservation reservation = new Reservation(null, "관리자", FIXED_TODAY.minusDays(7), PAST_TIME, THEME);

        assertThatCode(() -> policy.validate(reservation, FIXED_NOW)).doesNotThrowAnyException();
    }

    @Test
    void 오늘_날짜의_지난_시간도_예약할_수_있다() {
        Reservation reservation = new Reservation(null, "관리자", FIXED_TODAY, PAST_TIME, THEME);

        assertThatCode(() -> policy.validate(reservation, FIXED_NOW)).doesNotThrowAnyException();
    }
}
