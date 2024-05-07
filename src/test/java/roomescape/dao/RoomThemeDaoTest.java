package roomescape.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static roomescape.TestFixture.RESERVATION_TIME_FIXTURE;
import static roomescape.TestFixture.ROOM_THEME_FIXTURE;

import io.restassured.RestAssured;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import roomescape.domain.Name;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.RoomTheme;
import roomescape.exception.InvalidInputException;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RoomThemeDaoTest {
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

    @DisplayName("저장된 모든 테마를 보여준다.")
    @Test
    void findAll() {
        // given & when
        List<RoomTheme> roomThemes = roomThemeDao.findAll();
        // then
        assertThat(roomThemes).isEmpty();
    }

    @DisplayName("테마를 랭킹순으로 보여준다.")
    @Test
    void findAllRanking() {
        // given
        ReservationTime reservationTime = reservationTimeDao.save(RESERVATION_TIME_FIXTURE);
        RoomTheme roomTheme = roomThemeDao.save(ROOM_THEME_FIXTURE);
        roomThemeDao.save(new RoomTheme("a", "b", "c"));
        reservationDao.save(new Reservation(
                new Name("브라운"), LocalDate.now().minusDays(2), reservationTime, roomTheme));
        // when
        List<RoomTheme> ranking = roomThemeDao.findAllRanking();
        // then
        assertThat(ranking).hasSize(1);
        assertThat(ranking.get(0).getName()).isEqualTo(roomTheme.getName());
    }

    @DisplayName("해당 id의 테마를 보여준다.")
    @Test
    void findById() {
        // given
        RoomTheme roomTheme = ROOM_THEME_FIXTURE;
        RoomTheme savedRoomTheme = roomThemeDao.save(roomTheme);
        // when
        RoomTheme foundRoomTheme = roomThemeDao.findById(savedRoomTheme.getId());
        // then
        assertAll(
                () -> assertThat(foundRoomTheme.getId()).isEqualTo(savedRoomTheme.getId()),
                () -> assertThat(foundRoomTheme.getName()).isEqualTo(savedRoomTheme.getName()),
                () -> assertThat(foundRoomTheme.getDescription()).isEqualTo(
                        savedRoomTheme.getDescription()),
                () -> assertThat(foundRoomTheme.getThumbnail()).isEqualTo(
                        savedRoomTheme.getThumbnail())
        );
    }

    @DisplayName("해당 id의 테마가 없는 경우, 예외가 발생한다.")
    @Test
    void findByNotExistingId() {
        assertThatThrownBy(() -> roomThemeDao.findById(1L))
                .isInstanceOf(InvalidInputException.class)
                .hasMessage("해당 테마가 존재하지 않습니다.");
    }

    @DisplayName("테마를 저장한다.")
    @Test
    void save() {
        // given & when
        RoomTheme savedRoomTheme = roomThemeDao.save(ROOM_THEME_FIXTURE);
        // then
        assertAll(
                () -> assertThat(roomThemeDao.findAll()).hasSize(1),
                () -> assertThat(savedRoomTheme.getName()).isEqualTo(ROOM_THEME_FIXTURE.getName()),
                () -> assertThat(savedRoomTheme.getDescription())
                        .isEqualTo(ROOM_THEME_FIXTURE.getDescription()),
                () -> assertThat(savedRoomTheme.getThumbnail())
                        .isEqualTo(ROOM_THEME_FIXTURE.getThumbnail())
        );
    }

    @DisplayName("테마를 삭제한다.")
    @Test
    void deleteTheme() {
        // given
        RoomTheme roomTheme = ROOM_THEME_FIXTURE;
        RoomTheme savedRoomTheme = roomThemeDao.save(roomTheme);
        // when
        roomThemeDao.deleteById(savedRoomTheme.getId());
        // then
        assertThat(roomThemeDao.findAll()).isEmpty();
    }

    @DisplayName("삭제 대상이 존재하지 않으면 false를 반환한다.")
    @Test
    void returnFalseWhenNotDeleted() {
        // given & when
        boolean deleted = roomThemeDao.deleteById(1L);
        // then
        assertThat(deleted).isFalse();
    }
}
