package roomescape.reservation.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.time.LocalTime;
import org.junit.jupiter.api.Test;
import roomescape.error.ReservationException;
import roomescape.reservationtime.domain.ReservationTime;
import roomescape.theme.domain.Theme;

class ReservationTest {

    private final String DEFAULT_NAME = "홍길동";
    private final Theme DEFAULT_THEME = new Theme(1L, "테마", "설명", "썸네일");

    @Test
    void 예약_시간이_현재_이후면_객체가_정상_생성된다() {
        // given
        LocalDate today = LocalDate.now();
        LocalTime oneMinuteLater = LocalTime.now().plusMinutes(1);
        ReservationTime futureTime = new ReservationTime(1L, oneMinuteLater);

        // when
        // then
        assertThatCode(() ->
                new Reservation(DEFAULT_NAME, today, futureTime, DEFAULT_THEME)
        ).doesNotThrowAnyException();
    }

    @Test
    void 예약_시간이_현재_이전이면_ReservationException이_발생한다() {
        // given
        LocalDate today = LocalDate.now();
        LocalTime oneMinuteBefore = LocalTime.now().minusMinutes(1);
        ReservationTime pastTime = new ReservationTime(1L, oneMinuteBefore);

        // when
        // then
        assertThatThrownBy(() ->
                new Reservation(DEFAULT_NAME, today, pastTime, DEFAULT_THEME)
        )
                .isInstanceOf(ReservationException.class)
                .hasMessage("예약은 현재 시간 이후로 가능합니다.");
    }

    @Test
    void 새_예약의_id_필드는_null이다() {
        // given
        LocalDate today = LocalDate.now();
        LocalTime later = LocalTime.now().plusMinutes(5);
        ReservationTime rt = new ReservationTime(1L, later);
        Reservation reservation = new Reservation(DEFAULT_NAME, today, rt, DEFAULT_THEME);

        // when
        // then
        assertThat(reservation.getId()).isNull();
    }

    @Test
    void id_필드를_제외한_필드가_null이면_예외처리() {
        // given
        // when
        // then
        assertThatThrownBy(() -> new Reservation(null, null, null, null, null))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
