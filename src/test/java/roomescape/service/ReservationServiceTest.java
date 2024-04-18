package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.dto.ReservationAddRequest;


class ReservationServiceTest {

    @DisplayName("모든 예약 목록을 찾습니다.")
    @Test
    void should_find_all_reservation() {
        ReservationService reservationService = new ReservationService(new FakeReservationDao(
                Arrays.asList(
                        new Reservation(1L, "dodo", LocalDate.of(2020, 12, 12),
                                new ReservationTime(1L, LocalTime.of(12, 12))))
        ));
        int expectedSize = 1;

        int actualSize = reservationService.findAllReservation().size();

        assertThat(actualSize).isEqualTo(expectedSize);
    }

    @DisplayName("예약을 추가하고 저장된 예약을 반환합니다.")
    @Test
    void should_add_reservation() {
        ReservationService reservationService = new ReservationService(new FakeReservationDao());
        Reservation expectedReservation = new Reservation(1L, "dodo", LocalDate.of(2020, 12, 12),
                new ReservationTime(1L, LocalTime.of(10, 0)));

        Reservation actualReservation = reservationService.addReservation(
                new ReservationAddRequest(LocalDate.of(2020, 12, 12), "dodo", 1L));

        assertThat(actualReservation).isEqualTo(expectedReservation);
    }

    @DisplayName("원하는 id의 예약을 삭제합니다.")
    @Test
    void should_true_when_remove_reservation_with_exist_id() {
        FakeReservationDao fakeReservationDao = new FakeReservationDao(
                Arrays.asList(
                        new Reservation(1L, "dodo", LocalDate.of(2020, 12, 12),
                                new ReservationTime(1L, LocalTime.of(12, 12))
                        )
                ));
        ReservationService reservationService = new ReservationService(fakeReservationDao);

        reservationService.removeReservation(1L);

        assertThat(fakeReservationDao.reservations.containsKey(1L)).isFalse();
    }

    @DisplayName("없는 id의 예약을 삭제하면 예외를 발생합니다.")
    @Test
    void should_false_when_remove_reservation_with_non_exist_id() {
        ReservationService reservationService = new ReservationService(new FakeReservationDao());

        assertThatThrownBy(() -> reservationService.removeReservation(1L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("해당 id를 가진 예약이 존재하지 않습니다.");
    }
}
