package roomescape.reservation.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import org.junit.jupiter.api.Test;
import roomescape.exception.BusinessException;
import roomescape.reservationTime.domain.ReservationTime;

class ReservationTest {

    @Test
    void 예약_된_시간이_과거라면_true() {
        // given
        LocalDate yesterday = LocalDate.now().minusDays(1);
        ReservationTime reservationTime = new ReservationTime(LocalTime.of(10,0));
        Reservation reservation = new Reservation("도우너", yesterday, reservationTime, 1L);

        // when
        boolean result = reservation.isPast(LocalDateTime.now());

        // then
        assertThat(result).isTrue();
    }

    @Test
    void 예약된_시간이_현재보다_미래면_false() {
        // given
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        ReservationTime reservationTime = new ReservationTime(LocalTime.of(10,0));
        Reservation reservation = new Reservation("도우너", tomorrow, reservationTime, 1L);

        // when
        boolean result = reservation.isPast(LocalDateTime.now());

        // then
        assertThat(result).isFalse();
    }

    @Test
    void 예약된_이름과_요청받은_이름이_다르면_false() {
        // given
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        ReservationTime reservationTime = new ReservationTime(LocalTime.of(10,0));
        Reservation reservation = new Reservation("도우너", tomorrow, reservationTime, 1L);

        String requestName = "둘리";

        // when
        boolean result = reservation.isOwnerBy(requestName);

        // then
        assertThat(result).isFalse();
    }

    @Test
    void 예약된_이름과_요청받은_이름이_같으면_true() {
        // given
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        ReservationTime reservationTime = new ReservationTime(LocalTime.of(10,0));
        Reservation reservation = new Reservation("도우너", tomorrow, reservationTime, 1L);

        String requestName = "도우너";

        // when
        boolean result = reservation.isOwnerBy(requestName);

        // then
        assertThat(result).isTrue();
    }

    @Test
    void 이름_입력이_빈값일_경우_예외() {
        // given
        String name = "";
        ReservationTime reservationTime = new ReservationTime(LocalTime.of(10,0));

        // when & then
        assertThatThrownBy(() -> new Reservation(name, LocalDate.now(), reservationTime, 1L))
                .isInstanceOf(BusinessException.class)
                .hasMessage("이름 형식이 잘못되었습니다.");
    }


    @Test
    void 이름_입력이_범위를_초과할_경우_예외() {
        // given
        String name = "가나다라마바사아자차카타파하가나다라마바사아자차카타파하";
        ReservationTime reservationTime = new ReservationTime(LocalTime.of(10,0));

        // when & then
        assertThatThrownBy(() -> new Reservation(name, LocalDate.now(), reservationTime, 1L))
                .isInstanceOf(BusinessException.class)
                .hasMessage("이름 형식이 잘못되었습니다.");

    }

}