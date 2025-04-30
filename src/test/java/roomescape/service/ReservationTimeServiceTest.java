package roomescape.service;

import java.time.LocalTime;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.dao.FakeReservationTimeDao;
import roomescape.dto.reservationtime.ReservationTimeRequestDto;
import roomescape.dto.reservationtime.ReservationTimeResponseDto;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ReservationTimeServiceTest {

    private ReservationTimeService reservationTimeService;
    private FakeReservationTimeDao fakeReservationTimeDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        fakeReservationTimeDao = new FakeReservationTimeDao(jdbcTemplate);
        reservationTimeService = new ReservationTimeService(fakeReservationTimeDao);
    }

    @Test
    void 시간을_저장한다() {
        // given
        ReservationTimeRequestDto request = new ReservationTimeRequestDto(LocalTime.of(10, 0).toString());

        // when
        ReservationTimeResponseDto response = reservationTimeService.saveTime(request);

        // then
        assertThat(response.id()).isNotNull();
        assertThat(response.startAt()).isEqualTo(LocalTime.of(10, 0));
    }

    @Test
    void 모든_시간을_조회한다() {
        // given
        reservationTimeService.saveTime(new ReservationTimeRequestDto(LocalTime.of(10, 0).toString()));
        reservationTimeService.saveTime(new ReservationTimeRequestDto(LocalTime.of(12, 0).toString()));

        // when
        List<ReservationTimeResponseDto> times = reservationTimeService.getAllTimes();

        // then
        assertThat(times).hasSize(2);
        assertThat(times).extracting("startAt")
                .containsExactlyInAnyOrder(LocalTime.of(10, 0), LocalTime.of(12, 0));
    }

    @Test
    void 시간을_삭제한다() {
        // given
        ReservationTimeResponseDto saved = reservationTimeService.saveTime(
                new ReservationTimeRequestDto(LocalTime.of(10, 0).toString())
        );

        // when
        reservationTimeService.deleteTime(saved.id());

        // then
        List<ReservationTimeResponseDto> times = reservationTimeService.getAllTimes();
        assertThat(times).isEmpty();
    }

}
