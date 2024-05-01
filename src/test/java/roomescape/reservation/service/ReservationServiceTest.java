package roomescape.reservation.service;

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
import roomescape.reservation.request.ReservationRequest;
import roomescape.time.domain.ReservationTime;
import roomescape.time.repository.ReservationTimeDao;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ReservationServiceTest {
    @Autowired
    private ReservationService reservationService;

    @Autowired
    private ReservationDao reservationDao;

    @Autowired
    private ReservationTimeDao reservationTimeDao;

    @Test
    @DisplayName("중복된 예약을 생성하면 예외가 발생한다.")
    void saveDuplicateException() {
        String date = "2024-05-12";
        String time = "15:30";
        long timeId = insertReservationTimeAndGetId(time);
        insertReservation("zeus", date, timeId);
        assertThatThrownBy(() -> reservationService.save(new ReservationRequest("pond", LocalDate.parse(date),1L)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Reservation already exists");
    }

    void insertReservation(String name, String date, long timeId) {
        ReservationTime time = reservationTimeDao.findById(timeId);
        reservationDao.save(new Reservation(0, name, LocalDate.parse(date), time));
    }

    long insertReservationTimeAndGetId(String time) {
        return reservationTimeDao.save(new ReservationTime(0, LocalTime.parse(time))).id();
    }
}
