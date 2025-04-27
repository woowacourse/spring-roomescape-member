package roomescape.reservation.application;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.reservation.application.repository.ReservationTimeRepository;
import roomescape.reservation.application.service.ReservationTimeService;
import roomescape.reservation.infrastructure.fake.FakeReservationTimeDao;
import roomescape.reservation.presentation.dto.ReservationTimeRequest;
import roomescape.reservation.presentation.dto.ReservationTimeResponse;

public class ReservationTimeServiceTest {
    private ReservationTimeService reservationTimeService;

    @BeforeEach
    void init(){
        ReservationTimeRepository reservationTimeRepository = new FakeReservationTimeDao();
        reservationTimeService = new ReservationTimeService(reservationTimeRepository);
    }

    @Test
    @DisplayName("예약 시간 추가 테스트")
    void createReservationTimeTest(){
        // given
        ReservationTimeRequest reservationTimeRequest = new ReservationTimeRequest(LocalTime.of(15, 40));

        // when
        ReservationTimeResponse reservationTime = reservationTimeService.createReservationTime(reservationTimeRequest);

        // then
        assertThat(reservationTime.getId()).isEqualTo(1L);
        assertThat(reservationTime.getStartAt()).isEqualTo(LocalTime.of(15, 40));
    }

    @Test
    @DisplayName("예약 시간 조회 테스트")
    void getReservationTimesTest(){
        // given
        ReservationTimeRequest reservationTimeRequest = new ReservationTimeRequest(LocalTime.of(15, 40));
        reservationTimeService.createReservationTime(reservationTimeRequest);

        // when - then
        assertThat(reservationTimeService.getReservationTimes().size()).isEqualTo(1);
    }

    @Test
    @DisplayName("예약 시간 삭제 테스트")
    void deleteReservationTimeTest(){
        // given
        ReservationTimeRequest reservationTimeRequest = new ReservationTimeRequest(LocalTime.of(15, 40));
        reservationTimeService.createReservationTime(reservationTimeRequest);

        // when
        reservationTimeService.deleteReservationTime(1L);

        // then
        assertThat(reservationTimeService.getReservationTimes().size()).isEqualTo(0);

    }
}
