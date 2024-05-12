package roomescape.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import roomescape.domain.ReservationTime;
import roomescape.dto.request.ReservationTimeAddRequest;
import roomescape.dto.response.ReservationTimeResponse;
import roomescape.repository.reservation.ReservationRepository;
import roomescape.repository.reservationtime.ReservationTimeRepository;

import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static roomescape.InitialDataFixture.RESERVATION_TIME_1;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Sql("/initial_test_data.sql")
class ReservationTimeServiceTest {

    @Autowired
    private ReservationTimeService reservationTimeService;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private ReservationTimeRepository reservationTimeRepository;
    private ReservationTime reservationTime;

    @BeforeEach
    void setup() {
        reservationTime = reservationTimeRepository.save(RESERVATION_TIME_1);
    }

    @Test
    @DisplayName("예약 가능 시간을 추가하고 id값을 붙여서 응답 DTO를 생성한다.")
    void addGetTime() {
        ReservationTimeAddRequest reservationTimeAddRequest = new ReservationTimeAddRequest(LocalTime.of(15, 0));

        ReservationTimeResponse reservationTimeResponse = reservationTimeService.addTime(reservationTimeAddRequest);

        assertThat(reservationTimeResponse.id()).isNotNull();
    }

    @Test
    @DisplayName("id에 맞는 예약 가능 시간을 조회한다.")
    void getGetTime() {
        ReservationTimeResponse timeResponse = reservationTimeService.getTime(reservationTime.getId());

        assertThat(timeResponse.startAt()).isEqualTo(reservationTime.getStartAt().toString());
    }

    @Test
    @DisplayName("id에 맞는 예약 가능 시간을 삭제한다.")
    void deleteGetTime() {
        Integer beforeCount = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM reservation_time", Integer.class);

        reservationTimeService.deleteTime(reservationTime.getId());

        Integer afterCount = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM reservation_time", Integer.class);

        assertThat(afterCount).isEqualTo(beforeCount - 1);
    }

    @Test
    @DisplayName("존재하는 시간을 추가하려는 경우 예외가 발생한다.")
    void saveSameTime() {
        ReservationTimeAddRequest timeAddRequest = new ReservationTimeAddRequest(reservationTime.getStartAt());

        assertThatThrownBy(() -> reservationTimeService.addTime(timeAddRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
