package roomescape.policy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import roomescape.command.ReservationSaveCommand;
import roomescape.domain.ReservationTime;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThatCode;

class AdminReservationSavePolicyTest {

    private static final LocalDate FIXED_TODAY = LocalDate.of(2026, 5, 8);
    private static final LocalDateTime FIXED_NOW = LocalDateTime.of(FIXED_TODAY, LocalTime.of(12, 0));
    private static final ReservationTime PAST_TIME = new ReservationTime(1L, LocalTime.of(9, 0));

    private AdminReservationSavePolicy policy;

    @BeforeEach
    void setUp() {
        policy = new AdminReservationSavePolicy();
    }

    @Test
    void 지난_날짜도_예약할_수_있다() {
        ReservationSaveCommand command = new ReservationSaveCommand("관리자", FIXED_TODAY.minusDays(7), 1L, 1L);

        assertThatCode(() -> policy.validate(command, PAST_TIME, FIXED_NOW)).doesNotThrowAnyException();
    }

    @Test
    void 오늘_날짜의_지난_시간도_예약할_수_있다() {
        ReservationSaveCommand command = new ReservationSaveCommand("관리자", FIXED_TODAY, 1L, 1L);

        assertThatCode(() -> policy.validate(command, PAST_TIME, FIXED_NOW)).doesNotThrowAnyException();
    }
}
