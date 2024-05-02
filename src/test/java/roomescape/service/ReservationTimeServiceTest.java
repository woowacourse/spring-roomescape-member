package roomescape.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import roomescape.domain.ReservationTime;
import roomescape.dto.ReservationTimeRequestDto;

import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@Sql(scripts = {"/test_schema.sql", "/test_data.sql"})
public class ReservationTimeServiceTest {

    @Autowired
    private final ReservationTimeService reservationTimeService;

    @Autowired
    public ReservationTimeServiceTest(ReservationTimeService reservationTimeService) {
        this.reservationTimeService = reservationTimeService;
    }

    @Test
    void getAllReservationTimesTest() {
        List<ReservationTime> reservationTimes = reservationTimeService.getAllReservationTimes();

        assertThat(reservationTimes.size()).isEqualTo(1);
    }

    @Test
    void insertReservationTimeTest() {
        ReservationTimeRequestDto reservationTimeRequestDto = new ReservationTimeRequestDto(LocalTime.parse("01:01"));
        ReservationTime reservationTime = reservationTimeService.insertReservationTime(reservationTimeRequestDto);

        assertThat(reservationTime.getStartAt()).isEqualTo("01:01");
    }

    @Test
    void deleteReservationTimeTest() {
        ReservationTimeRequestDto reservationTimeRequestDto = new ReservationTimeRequestDto(LocalTime.parse("01:01"));
        ReservationTime reservationTime = reservationTimeService.insertReservationTime(reservationTimeRequestDto);
        int sizeBeforeDelete = reservationTimeService.getAllReservationTimes().size();
        assertThatCode(() -> reservationTimeService.deleteReservationTime(reservationTime.getId())).doesNotThrowAnyException();
        assertThat(reservationTimeService.getAllReservationTimes().size()).isEqualTo(sizeBeforeDelete - 1);
    }

    @Test
    void deleteInvalidTimeIdTest() {
        assertThatThrownBy(() -> reservationTimeService.deleteReservationTime(1L))
                .isInstanceOf(IllegalStateException.class);
    }
}
