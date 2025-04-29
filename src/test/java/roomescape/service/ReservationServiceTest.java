package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.server.ResponseStatusException;
import roomescape.fake.FakeReservaionDao;
import roomescape.fake.FakeReservationTimeDao;
import roomescape.dao.ReservationDao;
import roomescape.dao.ReservationTimeDao;
import roomescape.dto.ReservationRequest;
import roomescape.dto.ReservationResponse;
import roomescape.model.ReservationTime;

class ReservationServiceTest {

    private ReservationService reservationService;
    private ReservationTimeDao reservationTimeDao;

    @BeforeEach
    void setUp() {
        ReservationDao reservationDao = new FakeReservaionDao();
        reservationTimeDao = new FakeReservationTimeDao();
        reservationService = new ReservationService(reservationDao, reservationTimeDao);
    }

    @Test
    void 예약을_정상적으로_추가() {
        ReservationTime savedTime = reservationTimeDao.save(new ReservationTime(null, LocalTime.of(10, 0)));
        ReservationRequest request = new ReservationRequest("이름", LocalDate.of(2025,12,16),savedTime.getId());

        ReservationResponse response = reservationService.addReservation(request);

        assertThat(response.id()).isNotNull();
        assertThat(response.name()).isEqualTo("이름");
        assertThat(response.date()).isEqualTo(LocalDate.of(2025,12,16).toString());
    }

    @Test
    void 예약_리스트를_정상적으로_조회() {
        ReservationTime savedTime = reservationTimeDao.save(new ReservationTime(null, LocalTime.of(10, 0)));
        ReservationRequest request = new ReservationRequest("이름", LocalDate.of(2025,12,16),savedTime.getId());

        reservationService.addReservation(request);
        List<ReservationResponse> reservations = reservationService.getReservations();
        assertThat(reservations.size()).isEqualTo(1);
    }

    @Test
    void 예약을_정상적으로_삭제() {
        ReservationTime savedTime = reservationTimeDao.save(new ReservationTime(null, LocalTime.of(10, 0)));
        ReservationRequest request = new ReservationRequest("이름", LocalDate.of(2025,12,16),savedTime.getId());
        ReservationResponse response = reservationService.addReservation(request);

        reservationService.deleteReservation(response.id());
        List<ReservationResponse> reservations = reservationService.getReservations();

        assertThat(reservations).isEmpty();
    }

    @Test
    void 예약이_존재하지_않으면_예외발생() {
        assertThatThrownBy(() -> reservationService.deleteReservation(999L))
                .isInstanceOf(ResponseStatusException.class);
    }
}
