package roomescape.policy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import roomescape.command.ReservationSaveCommand;
import roomescape.domain.ReservationTime;
import roomescape.exception.UnprocessableException;
import roomescape.exception.code.UnprocessableCode;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class UserReservationSavePolicyTest {

    private static final LocalDate FIXED_TODAY = LocalDate.of(2026, 5, 8);
    private static final LocalTime FIXED_NOW_TIME = LocalTime.of(12, 0);
    private static final LocalDateTime FIXED_NOW = LocalDateTime.of(FIXED_TODAY, FIXED_NOW_TIME);
    private static final ReservationTime FUTURE_TIME = new ReservationTime(1L, LocalTime.of(18, 0));
    private static final ReservationTime PAST_TIME = new ReservationTime(1L, LocalTime.of(9, 0));

    private UserReservationSavePolicy policy;

    @BeforeEach
    void setUp() {
        policy = new UserReservationSavePolicy();
    }

    @Test
    void 지난_날짜는_예외가_발생한다() {
        ReservationSaveCommand command = new ReservationSaveCommand("브라운", FIXED_TODAY.minusDays(1), 1L, 1L);

        assertThatThrownBy(() -> policy.validate(command, FUTURE_TIME, FIXED_NOW))
                .isInstanceOf(UnprocessableException.class)
                .hasMessage(UnprocessableCode.RESERVATION_PAST_DATE.getMessage());
    }

    @Test
    void 오늘_날짜의_지난_시간은_예외가_발생한다() {
        ReservationSaveCommand command = new ReservationSaveCommand("브라운", FIXED_TODAY, 1L, 1L);

        assertThatThrownBy(() -> policy.validate(command, PAST_TIME, FIXED_NOW))
                .isInstanceOf(UnprocessableException.class)
                .hasMessage(UnprocessableCode.RESERVATION_PAST_TIME.getMessage());
    }

    @Test
    void 오늘_날짜의_미래_시간은_예약할_수_있다() {
        ReservationSaveCommand command = new ReservationSaveCommand("브라운", FIXED_TODAY, 1L, 1L);

        assertThatCode(() -> policy.validate(command, FUTURE_TIME, FIXED_NOW)).doesNotThrowAnyException();
    }

    @Test
    void 미래_날짜는_예약할_수_있다() {
        ReservationSaveCommand command = new ReservationSaveCommand("브라운", FIXED_TODAY.plusDays(1), 1L, 1L);

        assertThatCode(() -> policy.validate(command, PAST_TIME, FIXED_NOW)).doesNotThrowAnyException();
    }
}
