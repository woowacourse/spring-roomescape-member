package roomescape.time.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.time.LocalTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

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
        insertReservationTime("10:00");
        ReservationTime reservationTime = new ReservationTime(0L, LocalTime.parse("10:00"));
        assertThatThrownBy(() -> reservationTimeService.save(reservationTime))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("StartAt already exists");
    }

//    @Test
//    @DisplayName("예약이 있는 시간을 삭제하려 할 경우 예외를 발생한다.")
//    void deleteException() {
//        long themeId = insertThemeAndGetId("name", "description", "thumbnail");
//        long timeId = insertReservationTimeAndGetId("10:00");
//        insertReservationTime("11:00");
//        insertReservation("brown", "2025-05-01", timeId, themeId);
//        assertThatThrownBy(() -> reservationTimeService.delete(timeId))
//                .isInstanceOf(IllegalArgumentException.class)
//                .hasMessage("Cannot delete a reservation that refers to that time");
//    }

    void insertReservationTime(String time) {
        insertReservationTimeAndGetId(time);
    }

    long insertReservationTimeAndGetId(String time) {
        return reservationTimeDao.save(new ReservationTime(0, LocalTime.parse(time))).id();
    }

//    void insertReservation(String name, String date, long timeId, long themeId) {
//        ReservationTime time = reservationTimeDao.findById(timeId);
//        Theme theme = themeDao.findById(themeId);
//        reservationDao.save(new Reservation3(name, LocalDate.parse(date), time, theme));
//    }

    long insertThemeAndGetId(String name, String description, String thumbnail) {
        return themeDao.save(new Theme(0, name, description, thumbnail)).id();
    }
}
