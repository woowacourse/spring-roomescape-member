package roomescape.time.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import roomescape.time.domain.ReservationTime;
import roomescape.time.repository.ReservationTimeDao;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ReservationTimeServiceTest {

    @Autowired
    private ReservationTimeService reservationTimeService;

    @Autowired
    private ReservationTimeDao reservationTimeDao;

    @Test
    @DisplayName("이미 존재하는 예약 시간을 생성할 경우, 예외를 발생한다.")
    void createException() {
        insertReservationTime("10:00");
        ReservationTime reservationTime = new ReservationTime(0L, LocalTime.parse("10:00"));
        assertThatThrownBy(() -> reservationTimeService.save(reservationTime))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("StartAt already exists");
    }

    void insertReservationTime(String time) {
        reservationTimeDao.save(new ReservationTime(0, LocalTime.parse(time)));
    }
}
