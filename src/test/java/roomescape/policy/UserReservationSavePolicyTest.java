package roomescape.policy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import roomescape.command.ReservationSaveCommand;

import java.time.Clock;
import java.time.LocalDate;
import java.time.ZoneId;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.assertThatCode;

class UserReservationSavePolicyTest {

    private static final LocalDate FIXED_TODAY = LocalDate.of(2026, 5, 8);
    private static final ZoneId ZONE = ZoneId.of("Asia/Seoul");

    private UserReservationSavePolicy policy;

    @BeforeEach
    void setUp() {
        Clock fixedClock = Clock.fixed(FIXED_TODAY.atStartOfDay(ZONE).toInstant(), ZONE);
        policy = new UserReservationSavePolicy(fixedClock);
    }

    @Test
    void 지난_날짜는_예외가_발생한다() {
        ReservationSaveCommand command = new ReservationSaveCommand("브라운", FIXED_TODAY.minusDays(1), 1L, 1L);

        assertThatThrownBy(() -> policy.validate(command))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 오늘_날짜는_예약할_수_있다() {
        ReservationSaveCommand command = new ReservationSaveCommand("브라운", FIXED_TODAY, 1L, 1L);

        assertThatCode(() -> policy.validate(command)).doesNotThrowAnyException();
    }

    @Test
    void 미래_날짜는_예약할_수_있다() {
        ReservationSaveCommand command = new ReservationSaveCommand("브라운", FIXED_TODAY.plusDays(1), 1L, 1L);

        assertThatCode(() -> policy.validate(command)).doesNotThrowAnyException();
    }
}
