package roomescape.reservation.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.time.LocalTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.reservation.application.repository.ReservationRepository;
import roomescape.reservation.application.repository.ReservationTimeRepository;
import roomescape.reservation.application.service.ReservationService;
import roomescape.reservation.infrastructure.fake.FakeReservationDao;
import roomescape.reservation.infrastructure.fake.FakeReservationTimeDao;
import roomescape.reservation.presentation.dto.ReservationRequest;
import roomescape.reservation.presentation.dto.ReservationResponse;
import roomescape.reservation.presentation.dto.ReservationTimeRequest;

public class ReservationServiceTest {
    private ReservationService reservationService;
    private ReservationTimeRepository reservationTimeRepository;

    @BeforeEach
    void init() {
        ReservationRepository reservationRepository = new FakeReservationDao();
        reservationTimeRepository = new FakeReservationTimeDao();
        reservationService = new ReservationService(reservationRepository, reservationTimeRepository);
    }

    @Test
    @DisplayName("예약 추가 테스트")
    void createReservationTest() {
        // given
        ReservationTimeRequest reservationTimeRequest = new ReservationTimeRequest(LocalTime.of(15, 40));
        reservationTimeRepository.insert(reservationTimeRequest.getStartAt());

        ReservationRequest reservationRequest = new ReservationRequest(
                LocalDate.of(2023, 8, 5),
                "브라운",
                1L
        );

        // when
        ReservationResponse reservationResponse = reservationService.createReservation(reservationRequest);

        // then
        assertThat(reservationResponse.getId()).isEqualTo(1L);
        assertThat(reservationResponse.getDate()).isEqualTo(LocalDate.of(2023, 8, 5));
        assertThat(reservationResponse.getName()).isEqualTo("브라운");
        assertThat(reservationResponse.getTime().getId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("예약 추가 시 예약 시간이 조회되지 않으면 예외가 발생한다")
    void createReservationExceptionTest(){
        // given
        ReservationRequest reservationRequest = new ReservationRequest(
                LocalDate.of(2023, 8, 5),
                "브라운",
                1L
        );

        // when - then
        assertThatThrownBy(() -> reservationService.createReservation(reservationRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("[ERROR] 예약 시간 정보를 찾을 수 없습니다.");
    }

    @Test
    @DisplayName("예약 전체 조회 테스트")
    void getReservationsTest(){
        // given
        ReservationTimeRequest reservationTimeRequest = new ReservationTimeRequest(LocalTime.of(15, 40));
        reservationTimeRepository.insert(reservationTimeRequest.getStartAt());

        ReservationRequest reservationRequest = new ReservationRequest(
                LocalDate.of(2023, 8, 5),
                "브라운",
                1L
        );
        reservationService.createReservation(reservationRequest);

        // when - then
        assertThat(reservationService.getReservations().size()).isEqualTo(1);
    }

    @Test
    @DisplayName("예약 조회 테스트")
    void getReservationTest(){
        // given
        ReservationTimeRequest reservationTimeRequest = new ReservationTimeRequest(LocalTime.of(15, 40));
        reservationTimeRepository.insert(reservationTimeRequest.getStartAt());

        ReservationRequest reservationRequest = new ReservationRequest(
                LocalDate.of(2023, 8, 5),
                "브라운",
                1L
        );
        reservationService.createReservation(reservationRequest);

        // when - then
        assertThat(reservationService.getReservations().size()).isEqualTo(1);
    }

    @Test
    @DisplayName("예약 삭제 테스트")
    void deleteReservationTest(){
        // given
        ReservationTimeRequest reservationTimeRequest = new ReservationTimeRequest(LocalTime.of(15, 40));
        reservationTimeRepository.insert(reservationTimeRequest.getStartAt());

        ReservationRequest reservationRequest = new ReservationRequest(
                LocalDate.of(2023, 8, 5),
                "브라운",
                1L
        );
        reservationService.createReservation(reservationRequest);

        // when
        reservationService.deleteReservation(1L);

        // then
        assertThat(reservationService.getReservations().size()).isEqualTo(0);
    }
}
