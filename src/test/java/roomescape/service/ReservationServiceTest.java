package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.dao.reservation.ReservationDao;
import roomescape.dao.resetvationTime.ReservationTimeDao;
import roomescape.domain.ReservationTime;
import roomescape.dto.request.ReservationCreateRequest;
import roomescape.dto.response.ReservationResponse;

class ReservationServiceTest {

    private final ReservationTimeDao reservationTimeDao = new InMemoryReservationTimeDao(new ArrayList<>());
    private final ReservationDao reservationDao = new InMemoryReservationDao(new ArrayList<>(), reservationTimeDao);
    private final ReservationService reservationService = new ReservationService(reservationDao,
            reservationTimeDao);

    @DisplayName("예약을 생성한다.")
    @Test
    void createTest() {

        // given
        ReservationTime savedReservationTime = reservationTimeDao.create(new ReservationTime(LocalTime.of(10, 10)));
        reservationService.create(
                new ReservationCreateRequest("체체", "2024-10-10", savedReservationTime.getId()));

        // when
        List<ReservationResponse> reservations = reservationService.findAll();

        // then
        assertThat(reservations.getFirst().name()).isEqualTo("체체");
    }

    @DisplayName("예약을 모두 찾는다.")
    @Test
    void findAllTest() {

        // given
        ReservationTime savedReservationTime = reservationTimeDao.create(new ReservationTime(LocalTime.of(10, 10)));
        reservationService.create(
                new ReservationCreateRequest("체체", "2024-10-10", savedReservationTime.getId()));
        reservationService.create(
                new ReservationCreateRequest("체체2", "2024-10-11", savedReservationTime.getId()));

        // when
        List<ReservationResponse> reservations = reservationService.findAll();

        // then
        assertThat(reservations.size()).isEqualTo(2);
    }

    @DisplayName("예약을 삭제한다.")
    @Test
    void deleteTest() {

        // given
        ReservationTime savedReservationTime = reservationTimeDao.create(new ReservationTime(LocalTime.of(10, 10)));
        reservationService.create(
                new ReservationCreateRequest("체체", "2024-10-10", savedReservationTime.getId()));

        // when
        reservationService.delete(savedReservationTime.getId());
        List<ReservationResponse> reservations = reservationService.findAll();

        // then
        assertThat(reservations.size()).isEqualTo(0);
    }
}
