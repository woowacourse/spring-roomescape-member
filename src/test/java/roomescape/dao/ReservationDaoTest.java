package roomescape.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static roomescape.TestFixture.DATE;
import static roomescape.TestFixture.RESERVATION_TIME_10AM;
import static roomescape.TestFixture.ROOM_THEME1;

import io.restassured.RestAssured;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.RoomTheme;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ReservationDaoTest {
    @LocalServerPort
    private int port;

    @Autowired
    private ReservationDao reservationDao;
    @Autowired
    private ReservationTimeDao reservationTimeDao;
    @Autowired
    private RoomThemeDao roomThemeDao;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        List<Reservation> reservations = reservationDao.findAll();
        for (Reservation reservation : reservations) {
            reservationDao.deleteById(reservation.getId());
        }
        List<ReservationTime> reservationTimes = reservationTimeDao.findAll();
        for (ReservationTime reservationTime : reservationTimes) {
            reservationTimeDao.deleteById(reservationTime.getId());
        }
        List<RoomTheme> roomThemes = roomThemeDao.findAll();
        for (RoomTheme roomTheme : roomThemes) {
            roomThemeDao.deleteById(roomTheme.getId());
        }
    }

    @DisplayName("존재하는 모든 예약을 보여준다.")
    @Test
    void findAll() {
        assertThat(reservationDao.findAll()).isEmpty();
    }

    @DisplayName("날짜와 시간이 같은 예약이 존재하는지 여부를 반환한다.")
    @Test
    void duplicatedReservationTest() {
        // given
        ReservationTime savedReservationTime = reservationTimeDao.save(RESERVATION_TIME_10AM);
        RoomTheme savedRoomTheme = roomThemeDao.save(ROOM_THEME1);
        boolean existsFalse
                = reservationDao.existsByDateTime(DATE, savedReservationTime.getId(), savedRoomTheme.getId());
        reservationDao.save(new Reservation("브라운", DATE,
                savedReservationTime, savedRoomTheme));
        // when
        boolean existsTrue
                = reservationDao.existsByDateTime(DATE, savedReservationTime.getId(), savedRoomTheme.getId());
        // then
        assertAll(
                () -> assertThat(existsFalse).isFalse(),
                () -> assertThat(existsTrue).isTrue()
        );
    }

    @DisplayName("예약을 저장한다.")
    @Test
    void save() {
        // given
        ReservationTime reservationTime = reservationTimeDao.save(RESERVATION_TIME_10AM);
        RoomTheme roomTheme = roomThemeDao.save(ROOM_THEME1);
        // when
        reservationDao.save(new Reservation(
                "브라운", DATE, reservationTime, roomTheme));
        // then
        assertThat(reservationDao.findAll()).hasSize(1);
    }

    @DisplayName("해당 id의 예약을 삭제한다.")
    @Test
    void deleteById() {
        // given
        ReservationTime reservationTime = reservationTimeDao.save(RESERVATION_TIME_10AM);
        RoomTheme roomTheme = roomThemeDao.save(ROOM_THEME1);
        Reservation savedReservation = reservationDao.save(
                new Reservation("aa", DATE, reservationTime, roomTheme));
        // when
        reservationDao.deleteById(savedReservation.getId());
        // then
        assertThat(reservationDao.findAll()).isEmpty();
    }

    @DisplayName("삭제 대상이 존재하면 true를 반환한다.")
    @Test
    void returnTrueWhenDeleted() {
        // given
        ReservationTime reservationTime = reservationTimeDao.save(RESERVATION_TIME_10AM);
        RoomTheme roomTheme = roomThemeDao.save(ROOM_THEME1);
        Reservation savedReservation = reservationDao.save(
                new Reservation("aa", DATE, reservationTime, roomTheme));
        // when
        boolean deleted = reservationDao.deleteById(savedReservation.getId());
        // then
        assertThat(deleted).isTrue();
    }

    @DisplayName("삭제 대상이 존재하지 않으면 false를 반환한다.")
    @Test
    void returnFalseWhenNotDeleted() {
        // given & when
        boolean deleted = reservationDao.deleteById(1L);
        // then
        assertThat(deleted).isFalse();
    }
}
