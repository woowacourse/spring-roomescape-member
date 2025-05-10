package roomescape.reservation.integration;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.reservation.dto.request.ReservationRequest.ReservationCreateRequest;
import roomescape.reservation.dto.response.ReservationResponse.ReservationCreateResponse;
import roomescape.reservation.dto.response.ReservationResponse.ReservationReadResponse;
import roomescape.reservation.entity.Reservation;
import roomescape.reservation.entity.ReservationTime;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.reservation.repository.ReservationTimeRepository;
import roomescape.reservation.service.ReservationService;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ReservationIntegrationTest {

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private ReservationTimeRepository reservationTimeRepository;

    @Test
    @Disabled
    @DisplayName("모든 예약을 DB에서 조회한다.")
    void getReservations() {
        // given
        long id = 1L;
        String name = "미소";
        LocalDate date = LocalDate.of(2025, 4, 21);
        ReservationTime time = new ReservationTime(
                1L,
                LocalTime.of(10, 0)
        );
        long themeId = 1L;
        Reservation reservation = new Reservation(
                id,
                name,
                date,
                time,
                themeId
        );
        reservationTimeRepository.save(time);
        reservationRepository.save(reservation);

        // when
        List<ReservationReadResponse> responses = reservationService.getAllReservations();

        // then
        ReservationReadResponse response = responses.getFirst();
        assertThat(response.id()).isEqualTo(id);
        assertThat(response.name()).isEqualTo(name);
        assertThat(response.date()).isEqualTo(date);
        assertThat(response.time()).isEqualTo(time);
        assertThat(response.time().id()).isEqualTo(themeId);
    }

    @Test
    @Disabled
    @DisplayName("예약을 DB에 저장한다.")
    void createReservation() {
        // given
        ReservationTime reservationTime = new ReservationTime(
                1L,
                LocalTime.of(10, 0)
        );
        reservationTimeRepository.save(reservationTime);
        LocalDate date = LocalDate.of(2025, 4, 21);
        String name = "미소";
        long timeId = 1L;
        long themeId = 1L;
        ReservationCreateRequest request = new ReservationCreateRequest(
                date,
                name,
                timeId,
                themeId
        );

        // when
        ReservationCreateResponse response = reservationService.createReservation(request);

        // then
        assertThat(response.id()).isEqualTo(1L);
        assertThat(response.name()).isEqualTo(name);
        assertThat(response.date()).isEqualTo(date);
        assertThat(response.time().id()).isEqualTo(timeId);
        assertThat(response.theme().id()).isEqualTo(themeId);
    }

    @Test
    @Disabled
    @DisplayName("예약을 DB에서 삭제한다.")
    void deleteReservation() {
        // given
        ReservationTime reservationTime = new ReservationTime(
                1L,
                LocalTime.of(10, 0)
        );
        Reservation reservation = new Reservation(
                1L,
                "미소",
                LocalDate.of(2025, 4, 21),
                reservationTime,
                1L
        );
        reservationTimeRepository.save(reservationTime);
        reservationRepository.save(reservation);

        // when
        reservationService.deleteReservation(1L);

        // then
        List<Reservation> reservations = reservationRepository.findAll();
        assertThat(reservations.size()).isEqualTo(0);
    }
}
