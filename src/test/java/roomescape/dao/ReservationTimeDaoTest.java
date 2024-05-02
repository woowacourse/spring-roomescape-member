package roomescape.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static roomescape.TestFixture.DATE_FIXTURE;
import static roomescape.TestFixture.RESERVATION_TIME_FIXTURE;
import static roomescape.TestFixture.ROOM_THEME_FIXTURE;
import static roomescape.TestFixture.TIME_FIXTURE;

import io.restassured.RestAssured;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.dao.EmptyResultDataAccessException;
import roomescape.domain.Name;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.RoomTheme;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ReservationTimeDaoTest {
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

    @DisplayName("모든 예약 시간을 보여준다")
    @Test
    void findAll() {
        assertThat(reservationTimeDao.findAll()).isEmpty();
    }

    @DisplayName("예약 시간을 저장한다.")
    @Test
    void save() {
        // given & when
        reservationTimeDao.save(RESERVATION_TIME_FIXTURE);
        // then
        assertThat(reservationTimeDao.findAll()).hasSize(1);
    }

    @DisplayName("해당 id의 예약 시간을 보여준다.")
    @Test
    void findById() {
        // given & when
        ReservationTime reservationTime = reservationTimeDao.save(RESERVATION_TIME_FIXTURE);
        Long id = reservationTime.getId();
        // then
        assertThat(reservationTimeDao.findById(id).getStartAt()).isEqualTo(TIME_FIXTURE);
    }

    @DisplayName("해당 id의 예약 시간이 없는 경우, 예외가 발생한다.")
    @Test
    void findByNotExistingId() {
        assertThatThrownBy(() -> reservationTimeDao.findById(1L))
                .isInstanceOf(EmptyResultDataAccessException.class);
    }

    @DisplayName("중복된 예약 시간이 존재하는 지 여부를 반환한다.")
    @Test
    void existsByStartAt() {
        // given
        boolean existsFalse = reservationTimeDao.existByStartAt(TIME_FIXTURE);
        reservationTimeDao.save(RESERVATION_TIME_FIXTURE);
        // when
        boolean existsTrue = reservationTimeDao.existByStartAt(TIME_FIXTURE);
        // then
        assertAll(
                () -> assertThat(existsFalse).isFalse(),
                () -> assertThat(existsTrue).isTrue()
        );
    }

    @DisplayName("해당 id의 예약 시간을 삭제한다.")
    @Test
    void deleteById() {
        // given
        ReservationTime reservationTime = reservationTimeDao.save(RESERVATION_TIME_FIXTURE);
        // when
        reservationTimeDao.deleteById(reservationTime.getId());
        // then
        assertThat(reservationTimeDao.findAll()).isEmpty();
    }

    @DisplayName("해당 id의 예약 시간을 삭제하는 경우, 그 id를 참조하는 예약도 삭제한다.")
    @Test
    void deleteByIdDeletesReservationAlso() {
        // given
        ReservationTime reservationTime = reservationTimeDao.save(RESERVATION_TIME_FIXTURE);
        RoomTheme roomTheme = roomThemeDao.save(ROOM_THEME_FIXTURE);
        reservationDao.save(new Reservation(new Name("aa"), DATE_FIXTURE,
                reservationTime, roomTheme));
        // when
        reservationTimeDao.deleteById(reservationTime.getId());
        // then
        assertThat(reservationDao.findAll()).isEmpty();
    }

    @DisplayName("삭제 대상이 존재하면 true를 반환한다.")
    @Test
    void returnTrueWhenDeleted() {
        // given
        ReservationTime reservationTime = reservationTimeDao.save(RESERVATION_TIME_FIXTURE);
        // when
        boolean deleted = reservationTimeDao.deleteById(reservationTime.getId());
        // then
        assertThat(deleted).isTrue();
    }

    @DisplayName("삭제 대상이 존재하지 않으면 false를 반환한다.")
    @Test
    void returnFalseWhenNotDeleted() {
        // given & when
        boolean deleted = reservationTimeDao.deleteById(1L);
        // then
        assertThat(deleted).isFalse();
    }
}
