package roomescape.reservation.integration;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.reservation.dto.request.ReservationTimeRequest.ReservationTimeCreateRequest;
import roomescape.reservation.dto.response.ReservationTimeResponse.ReservationTimeCreateResponse;
import roomescape.reservation.dto.response.ReservationTimeResponse.ReservationTimeReadResponse;
import roomescape.reservation.entity.ReservationTime;
import roomescape.reservation.repository.ReservationTimeRepository;
import roomescape.reservation.service.ReservationTimeService;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ReservationTimeIntegrationTest {

    @Autowired
    private ReservationTimeService reservationTimeService;

    @Autowired
    private ReservationTimeRepository reservationTimeRepository;

    @Test
    @DisplayName("모든 시간을 DB에서 조회한다.")
    @Disabled
    void getReservationTimes() {
        // given
        long id = 1L;
        LocalTime startAt = LocalTime.of(10, 0);
        ReservationTime reservationTime = new ReservationTime(
                id,
                startAt
        );
        reservationTimeRepository.save(reservationTime);

        // when
        List<ReservationTimeReadResponse> responses = reservationTimeService.getAllTimes();

        // then
        ReservationTimeReadResponse response = responses.getFirst();
        assertThat(response.id()).isEqualTo(id);
        assertThat(response.startAt()).isEqualTo(reservationTime);
    }

    @Test
    @Disabled
    @DisplayName("시간을 DB에 저장한다.")
    void createReservationTime() {
        // given
        LocalTime time = LocalTime.of(10, 0);
        ReservationTimeCreateRequest request = new ReservationTimeCreateRequest(
                time
        );

        // when
        ReservationTimeCreateResponse response = reservationTimeService.createTime(request);

        // then
        assertThat(response.id()).isEqualTo(1L);
        assertThat(response.startAt()).isEqualTo(time);
    }

    @Test
    @DisplayName("시간을 DB에서 삭제한다.")
    void deleteReservationTime() {
        // given
        ReservationTime reservationTime = new ReservationTime(
                1L,
                LocalTime.of(10, 0)
        );
        reservationTimeRepository.save(reservationTime);

        // when
        reservationTimeService.deleteTime(1L);

        // then
        List<ReservationTime> reservationTimes = reservationTimeRepository.findAll();
        assertThat(reservationTimes.size()).isEqualTo(0);
    }
}
