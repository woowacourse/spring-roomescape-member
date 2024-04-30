package roomescape.reservation.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.reservation.dao.FakeReservationTimeDao;
import roomescape.reservation.domain.ReservationTime;
import roomescape.reservation.domain.repository.ReservationTimeRepository;
import roomescape.reservation.dto.ReservationTimeRequest;
import roomescape.reservation.dto.ReservationTimeResponse;

@DisplayName("예약 시간 로직 테스트")
class ReservationTimeServiceTest {
    ReservationTimeRepository reservationTimeRepository;

    ReservationTimeService reservationTimeService;

    @BeforeEach
    void setUp() {
        reservationTimeRepository = new FakeReservationTimeDao();
        reservationTimeService = new ReservationTimeService(reservationTimeRepository);
    }

    @DisplayName("예약 시간 생성에 성공한다.")
    @Test
    void create() {
        //given
        LocalTime localTime = LocalTime.MIDNIGHT;
        ReservationTimeRequest reservationTimeRequest = new ReservationTimeRequest(localTime);

        //when
        ReservationTimeResponse reservationTimeResponse = reservationTimeService.create(reservationTimeRequest);

        //then
        assertThat(reservationTimeResponse.startAt()).isEqualTo(localTime);
        assertThat(reservationTimeRepository.findAll()).hasSize(1);
    }

    @DisplayName("예약 시간 조회에 성공한다.")
    @Test
    void findAll() {
        //given
        long id = 1L;
        LocalTime localTime = LocalTime.MIDNIGHT;
        reservationTimeRepository.save(new ReservationTime(id, localTime));

        //when
        List<ReservationTimeResponse> reservationTimes = reservationTimeService.findAll();

        //then
        assertThat(reservationTimes).hasSize(1);
    }

    @DisplayName("예약 시간 삭제에 성공한다.")
    @Test
    void delete() {
        //given
        long id = 1L;
        LocalTime localTime = LocalTime.MIDNIGHT;
        reservationTimeRepository.save(new ReservationTime(id, localTime));

        //when
        reservationTimeService.delete(id);

        //then
        assertThat(reservationTimeRepository.findAll()).hasSize(0);
    }
}
