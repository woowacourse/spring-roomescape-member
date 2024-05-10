package roomescape.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import roomescape.domain.ReservationTime;
import roomescape.dto.ReservationTimeRequestDto;
import roomescape.exception.NotRemovableByConstraintException;
import roomescape.exception.WrongStateException;

import java.time.LocalDate;
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

    @DisplayName("모든 예약 시간을 조회한다.")
    @Test
    void getAllReservationTimesTest() {
        List<ReservationTime> reservationTimes = reservationTimeService.getAllReservationTimes();

        assertThat(reservationTimes.size()).isEqualTo(1);
    }

    @DisplayName("예약 시간을 추가한다.")
    @Test
    void insertReservationTimeTest() {
        ReservationTimeRequestDto reservationTimeRequestDto = new ReservationTimeRequestDto(LocalTime.parse("01:01"));
        ReservationTime reservationTime = reservationTimeService.insertReservationTime(reservationTimeRequestDto);

        assertThat(reservationTime.getStartAt()).isEqualTo("01:01");
    }

    @DisplayName("예약 시간 ID를 이용하여 시간을 삭제한다.")
    @Test
    void deleteReservationTimeTest() {
        ReservationTimeRequestDto reservationTimeRequestDto = new ReservationTimeRequestDto(LocalTime.parse("01:01"));
        ReservationTime reservationTime = reservationTimeService.insertReservationTime(reservationTimeRequestDto);
        int sizeBeforeDelete = reservationTimeService.getAllReservationTimes().size();
        assertThatCode(() -> reservationTimeService.deleteReservationTime(reservationTime.getId())).doesNotThrowAnyException();
        assertThat(reservationTimeService.getAllReservationTimes().size()).isEqualTo(sizeBeforeDelete - 1);
    }

    @DisplayName("예약이 존재하는 시간은 삭제할 수 없다.")
    @Test
    void deleteInvalidTimeIdTest() {
        assertThatThrownBy(() -> reservationTimeService.deleteReservationTime(1L))
                .isInstanceOf(NotRemovableByConstraintException.class);
    }

    @DisplayName("예약이 가능한 시간인지 확인한다.")
    @Test
    void isBookedTest() {
        boolean actualIsBooked = reservationTimeService.isBooked(LocalDate.now().minusDays(1).toString(), 1L, 1L);

        assertThat(actualIsBooked).isEqualTo(true);
    }

    @DisplayName("허용된 날짜 이전이 입력된 경우 예외를 발생한다.")
    @Test
    void dateLimitTest() {
        assertThatThrownBy(() -> reservationTimeService.isBooked("1999-01-01", 1L, 1L))
                .isInstanceOf(WrongStateException.class)
                .hasMessage("2020년 이전의 날짜는 입력할 수 없습니다.");
    }
}
