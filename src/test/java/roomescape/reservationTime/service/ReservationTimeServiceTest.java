package roomescape.reservationTime.service;

import java.time.LocalDate;
import java.time.LocalTime;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.domain.ReservationRepository;
import roomescape.reservation.service.FakeReservationRepository;
import roomescape.reservation.service.FakeReservationTimeRepository;
import roomescape.reservationTime.domain.ReservationTime;
import roomescape.reservationTime.domain.ReservationTimeRepository;

class ReservationTimeServiceTest {
    private ReservationTimeService reservationTimeService;

    @BeforeEach
    void beforeEach() {
        ReservationTime reservationTime1 = new ReservationTime(null, LocalTime.of(10, 0));

        ReservationTimeRepository reservationTimeRepository = new FakeReservationTimeRepository();
        Long id = reservationTimeRepository.save(reservationTime1);
        reservationTime1 = reservationTimeRepository.findById(id);
        ReservationRepository reservationRepository = new FakeReservationRepository();
        reservationRepository.save(new Reservation(null, "홍길동", LocalDate.of(2024, 10, 6), reservationTime1, null));

        reservationTimeService = new ReservationTimeService(reservationRepository, reservationTimeRepository);
    }

    @Test
    @DisplayName("이미 존재하는 예약이 있는 경우 예역 시간을 삭제할 수 없다.")
    void can_not_delete_when_reservation_exists() {
        Assertions.assertThatThrownBy(() -> reservationTimeService.deleteReservationTimeById(1L))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
