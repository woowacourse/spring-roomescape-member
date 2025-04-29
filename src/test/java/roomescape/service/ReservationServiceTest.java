package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDate;
import java.time.LocalTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.domain.ReservationTime;
import roomescape.dto.ReservationRequest;
import roomescape.dto.ReservationResponse;
import roomescape.fake.FakeReservationDao;
import roomescape.fake.FakeReservationTimeDao;

public class ReservationServiceTest {

    private ReservationService reservationService;

    @BeforeEach
    void setUp() {
        FakeReservationDao reservationDao = new FakeReservationDao();
        FakeReservationTimeDao reservationTimeDao = new FakeReservationTimeDao();
        reservationService = new ReservationService(reservationDao, reservationTimeDao);

        reservationTimeDao.addTime(new ReservationTime(1L, LocalTime.of(10, 0)));
    }

    @Test
    @DisplayName("예약 추가를 할 수 있다.")
    void addReservation() {
        ReservationRequest request = new ReservationRequest(LocalDate.of(3000, 4, 26), "사나", 1L);
        ReservationResponse actual = reservationService.addReservation(request);

        assertAll(() -> {
            assertThat(actual.id()).isEqualTo(1L);
            assertThat(actual.name()).isEqualTo("사나");
            assertThat(actual.date()).isEqualTo(LocalDate.of(3000, 4, 26));
            assertThat(actual.time().getId()).isEqualTo(1L);
        });
    }

    @Test
    @DisplayName("모든 예약 정보를 가져올 수 있다.")
    void findAllReservations() {
        ReservationRequest request1 = new ReservationRequest(LocalDate.of(3000, 4, 26), "사나", 1L);
        ReservationRequest request2 = new ReservationRequest(LocalDate.of(3000, 4, 28), "앤지", 1L);

        reservationService.addReservation(request1);
        reservationService.addReservation(request2);

        assertThat(reservationService.findAllReservations()).hasSize(2);
    }

    @Test
    @DisplayName("예약을 id를 통해 제거할 수 있다.")
    void removeReservation() {
        ReservationRequest request = new ReservationRequest(LocalDate.of(3000, 4, 26), "사나", 1L);
        reservationService.addReservation(request);

        reservationService.removeReservation(1L);

        assertThat(reservationService.findAllReservations()).hasSize(0);
    }
}
