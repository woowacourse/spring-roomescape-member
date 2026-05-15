package roomescape.reservation.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.time.LocalTime;
import org.junit.jupiter.api.Test;
import roomescape.exception.BusinessRuleException;
import roomescape.exception.OwnershipViolationException;
import roomescape.reservationtime.domain.ReservationTime;
import roomescape.theme.domain.Theme;

class ReservationTest {

    private static final ReservationTime FUTURE_TIME = new ReservationTime(1L, LocalTime.of(10, 0));
    private static final ReservationTime PAST_TIME = new ReservationTime(2L, LocalTime.of(0, 0));
    private static final Theme THEME = new Theme(1L, "테마A", "설명", "https://thumb.png");

    @Test
    void 예약자_이름이_일치하면_검증을_통과한다() {
        Reservation reservation = new Reservation(1L, "브라운", LocalDate.now().plusDays(1), FUTURE_TIME, THEME);

        assertThatCode(() -> reservation.validateOwner("브라운"))
                .doesNotThrowAnyException();
    }

    @Test
    void 예약자_이름이_다르면_예외가_발생한다() {
        Reservation reservation = new Reservation(1L, "브라운", LocalDate.now().plusDays(1), FUTURE_TIME, THEME);

        assertThatThrownBy(() -> reservation.validateOwner("코니"))
                .isInstanceOf(OwnershipViolationException.class)
                .hasMessage("예약자 이름이 일치하지 않습니다.");
    }

    @Test
    void 미래_예약이면_활성_검증을_통과한다() {
        Reservation reservation = new Reservation(1L, "브라운", LocalDate.now().plusDays(1), FUTURE_TIME, THEME);

        assertThatCode(reservation::validateIsActive)
                .doesNotThrowAnyException();
    }

    @Test
    void 과거_예약이면_활성_검증에서_예외가_발생한다() {
        Reservation reservation = new Reservation(1L, "브라운", LocalDate.now().minusDays(1), PAST_TIME, THEME);

        assertThatThrownBy(reservation::validateIsActive)
                .isInstanceOf(BusinessRuleException.class)
                .hasMessage("이미 지난 예약은 취소하거나 변경할 수 없습니다.");
    }

    @Test
    void 같은_날짜와_시간이면_isSameSlot이_true를_반환한다() {
        LocalDate date = LocalDate.of(2099, 12, 31);
        Reservation reservation = new Reservation(1L, "브라운", date, FUTURE_TIME, THEME);

        assertThat(reservation.isSameSlot(date, 1L)).isTrue();
    }

    @Test
    void 날짜가_다르면_isSameSlot이_false를_반환한다() {
        Reservation reservation = new Reservation(1L, "브라운", LocalDate.of(2099, 12, 31), FUTURE_TIME, THEME);

        assertThat(reservation.isSameSlot(LocalDate.of(2099, 12, 30), 1L)).isFalse();
    }

    @Test
    void 시간이_다르면_isSameSlot이_false를_반환한다() {
        LocalDate date = LocalDate.of(2099, 12, 31);
        Reservation reservation = new Reservation(1L, "브라운", date, FUTURE_TIME, THEME);

        assertThat(reservation.isSameSlot(date, 2L)).isFalse();
    }
}