package roomescape.domain.reservation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import java.time.LocalDate;
import java.time.LocalTime;
import org.junit.jupiter.api.Test;
import roomescape.domain.reservationdate.ReservationDate;
import roomescape.domain.reservationtime.ReservationTime;
import roomescape.support.exception.RoomescapeException;

class ReservationTest {

    @Test
    void id가_없는_예약을_생성한다() {
        // given
        String name = "보예";
        ReservationDate date = ReservationDate.createWithoutId(LocalDate.of(2023, 8, 5));
        ReservationTime time = ReservationTime.createWithoutId(LocalTime.of(15, 40));

        // when
        Reservation reservation = Reservation.createWithoutId(name, date, time);

        // then
        assertSoftly(softly -> {
                softly.assertThat(reservation.getId()).isNull();
                softly.assertThat(reservation.getName()).isEqualTo(name);
                softly.assertThat(reservation.getDate()).isEqualTo(date);
                softly.assertThat(reservation.getTime()).isEqualTo(time);
            }
        );
    }

    @Test
    void id를_부여한_예약을_생성한다() {
        // given
        ReservationTime time = ReservationTime.createWithoutId(LocalTime.of(15, 40));
        ReservationDate date = ReservationDate.createWithoutId(LocalDate.of(2023, 8, 5));
        Reservation reservation = Reservation.createWithoutId(
            "보예",
            date,
            time
        );

        // when
        Reservation reservationWithId = Reservation.of(
            1L,
            reservation.getName(),
            reservation.getDate(),
            reservation.getTime()
        );

        // then
        assertSoftly(softly -> {
                assertThat(reservationWithId.getId()).isEqualTo(1L);
                assertThat(reservationWithId.getName()).isEqualTo("보예");
                assertThat(reservationWithId.getDate()).isEqualTo(date);
                assertThat(reservationWithId.getTime()).isEqualTo(time);
            }
        );
    }

    @Test
    void DB에서_조회한_예약을_생성한다() {
        // given
        long id = 1L;
        String name = "보예";
        ReservationDate date = ReservationDate.of(2L, LocalDate.of(2023, 8, 5));
        ReservationTime time = ReservationTime.createWithoutId(LocalTime.of(15, 40));

        // when
        Reservation reservation = Reservation.of(id, name, date, time);

        // then
        assertSoftly(softly -> {
                assertThat(reservation.getId()).isEqualTo(id);
                assertThat(reservation.getName()).isEqualTo(name);
                assertThat(reservation.getDate()).isEqualTo(date);
                assertThat(reservation.getTime()).isEqualTo(time);
            }
        );
    }

    @Test
    void 이름이_null이면_예외가_발생한다() {
        // given
        String name = null;
        ReservationDate date = ReservationDate.createWithoutId(LocalDate.of(2023, 8, 5));
        ReservationTime time = ReservationTime.createWithoutId(LocalTime.of(15, 40));

        // when & then
        assertThatThrownBy(() -> Reservation.createWithoutId(name, date, time))
            .isInstanceOf(RoomescapeException.class)
            .hasMessage("이름은 비어 있을 수 없습니다.");
    }

    @Test
    void 이름이_공백이면_예외가_발생한다() {
        // given
        String name = "            ";
        ReservationDate date = ReservationDate.createWithoutId(LocalDate.of(2023, 8, 5));
        ReservationTime time = ReservationTime.createWithoutId(LocalTime.of(15, 40));

        // when & then
        assertThatThrownBy(() -> Reservation.createWithoutId(name, date, time))
            .isInstanceOf(RoomescapeException.class)
            .hasMessage("이름은 비어 있을 수 없습니다.");
    }

    @Test
    void 날짜가_null이면_예외가_발생한다() {
        // given
        String name = "보예";
        ReservationDate date = null;
        ReservationTime time = ReservationTime.createWithoutId(LocalTime.of(15, 40));

        // when & hen
        assertThatThrownBy(() -> Reservation.createWithoutId(name, date, time))
            .isInstanceOf(RoomescapeException.class)
            .hasMessage("날짜는 필수입니다.");
    }

    @Test
    void 예약_시간이_null이면_예외가_발생한다() {
        // given
        String name = "보예";
        ReservationDate date = ReservationDate.createWithoutId(LocalDate.of(2023, 8, 5));
        ReservationTime time = null;

        // when & then
        assertThatThrownBy(() -> Reservation.createWithoutId(name, date, time))
            .isInstanceOf(RoomescapeException.class)
            .hasMessage("시간은 필수입니다.");
    }
}
