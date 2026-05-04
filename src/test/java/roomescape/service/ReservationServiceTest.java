package roomescape.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.repository.FakeReservationDao;
import roomescape.repository.FakeReservationTimeDao;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ReservationServiceTest {

    private ReservationService reservationService;
    private ReservationTime savedTime;

    @BeforeEach
    void setUp() {
        FakeReservationTimeDao fakeReservationTimeDao = new FakeReservationTimeDao();
        FakeReservationDao fakeReservationDao = new FakeReservationDao();
        reservationService = new ReservationService(fakeReservationDao, fakeReservationTimeDao);
        savedTime = fakeReservationTimeDao.save(ReservationTime.transientOf(LocalTime.of(10, 0)));
    }

    @Test
    @DisplayName("원시값을 받아 연관된 객체를 조회하여 조립한 뒤 예약을 생성한다.")
    void saveReservation() {
        LocalDate futureDate = LocalDate.now().plusDays(1);
        Reservation reservation = reservationService.saveReservation("브라운", futureDate, savedTime.id());
        assertThat(reservation.reservationTime().startAt()).isEqualTo(LocalTime.of(10, 0));
    }

    @Test
    @DisplayName("존재하는 예약을 식별자를 통해 삭제하면 목록에서 사라진다.")
    void removeReservation() {
        LocalDate futureDate = LocalDate.now().plusDays(1);
        Reservation reservation = reservationService.saveReservation("브라운", futureDate, savedTime.id());
        reservationService.removeReservation(reservation.id());
        assertThat(reservationService.allReservations()).isEmpty();
    }

    @Test
    @DisplayName("모든 예약 목록을 조회하여 반환한다.")
    void allReservations() {
        LocalDate futureDate = LocalDate.now().plusDays(1);
        reservationService.saveReservation("브라운", futureDate, savedTime.id());
        List<Reservation> reservations = reservationService.allReservations();
        assertThat(reservations).hasSize(1);
    }

    @Test
    @DisplayName("식별자를 통해 특정 예약 객체를 조회한다.")
    void findReservation() {
        LocalDate futureDate = LocalDate.now().plusDays(1);
        Reservation savedReservation = reservationService.saveReservation("브라운", futureDate, savedTime.id());
        Reservation foundReservation = reservationService.findReservation(savedReservation.id());
        assertThat(foundReservation.name()).isEqualTo("브라운");
    }
}
