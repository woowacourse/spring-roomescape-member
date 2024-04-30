package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import roomescape.dao.ReservationTimeRepository;
import roomescape.domain.ReservationTime;
import roomescape.exception.InvalidReservationException;
import roomescape.service.dto.ReservationTimeRequest;
import roomescape.service.dto.ReservationTimeResponse;

@SpringBootTest
class ReservationTimeServiceTest {
    @Autowired
    private ReservationTimeRepository reservationTimeRepository;
    @Autowired
    private ReservationTimeService reservationTimeService;

    @AfterEach
    void init() {
        for (final ReservationTime reservationTime : reservationTimeRepository.findAll()) {
            reservationTimeRepository.deleteById(reservationTime.getId());
        }
    }

    @DisplayName("새로운 예약 시간을 저장한다.")
    @Test
    void create() {
        //given
        String startAt = "10:00";
        ReservationTimeRequest reservationTimeRequest = new ReservationTimeRequest(startAt);

        //when
        ReservationTimeResponse result = reservationTimeService.create(reservationTimeRequest);

        //then
        assertAll(
                () -> assertThat(result.id()).isNotZero(),
                () -> assertThat(result.startAt()).isEqualTo(startAt)
        );
    }

    @DisplayName("모든 예약 시간 내역을 조회한다.")
    @Test
    void findAll() {
        //given
        String startAt = "10:00";
        ReservationTimeRequest reservationTimeRequest = new ReservationTimeRequest(startAt);
        reservationTimeService.create(reservationTimeRequest);

        //when
        List<ReservationTimeResponse> reservationTimes = reservationTimeService.findAll();

        //then
        assertThat(reservationTimes).hasSize(1);
    }

    @DisplayName("시간이 이미 존재하면 예외를 발생시킨다.")
    @Test
    void duplicatedTime() {
        //given
        String startAt = "10:00";
        ReservationTimeRequest reservationTimeRequest = new ReservationTimeRequest(startAt);
        reservationTimeService.create(reservationTimeRequest);

        //when&then
        assertThatThrownBy(() -> reservationTimeService.create(reservationTimeRequest))
                .isInstanceOf(InvalidReservationException.class)
                .hasMessage("이미 같은 시간이 존재합니다.");
    }
}
