package roomescape.time.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.time.LocalTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import roomescape.reservation.domain.Reservation;
import roomescape.reservation.repository.ReservationDao;
import roomescape.theme.domain.Theme;
import roomescape.theme.repository.ThemeDao;
import roomescape.time.domain.ReservationTime;
import roomescape.time.repository.ReservationTimeDao;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ReservationTimeServiceTest {

    @Autowired
    private ReservationTimeService reservationTimeService;

    @Autowired
    private ReservationTimeDao reservationTimeDao;

    @Autowired
    private ReservationDao reservationDao;

    @Autowired
    private ThemeDao themeDao;

    @Test
    @DisplayName("이미 존재하는 예약 시간을 생성할 경우, 예외를 발생한다.")
    void createException() {
        reservationTimeDao.save(new ReservationTime(LocalTime.parse("10:00")));
        ReservationTime reservationTime = new ReservationTime(LocalTime.parse("10:00"));
        assertThatThrownBy(() -> reservationTimeService.save(reservationTime))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("StartAt already exists");
    }

    @Test
    @DisplayName("예약이 있는 시간을 삭제하려 할 경우 예외를 발생한다.")
    void deleteException() {
        Reservation reservation = reservationDao.findById(1);
        ReservationTime time = reservation.time();

        assertThatThrownBy(() -> reservationTimeService.delete(time.id()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Cannot delete a reservation that refers to that time");
    }
}
