package roomescape.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static roomescape.TestFixture.DATE_FIXTURE;
import static roomescape.TestFixture.RESERVATION_TIME_FIXTURE;
import static roomescape.TestFixture.ROOM_THEME_FIXTURE;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.console.dao.InMemoryReservationDao;
import roomescape.console.db.InMemoryReservationDb;
import roomescape.console.db.InMemoryReservationTimeDb;
import roomescape.console.db.InMemoryRoomThemeDb;
import roomescape.domain.Name;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.RoomTheme;

class InMemoryReservationDaoTest {
    private InMemoryReservationTimeDb inMemoryReservationTimeDb;
    private InMemoryRoomThemeDb inMemoryRoomThemeDb;
    private ReservationDao reservationDao;

    @BeforeEach
    void setUp() {
        inMemoryReservationTimeDb = new InMemoryReservationTimeDb();
        inMemoryRoomThemeDb = new InMemoryRoomThemeDb();
        reservationDao = new InMemoryReservationDao(
                new InMemoryReservationDb());
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
        boolean existsFalse = reservationDao.existsByDateTime(DATE_FIXTURE, 1L);
        reservationDao.save(new Reservation(new Name("asd"),
                DATE_FIXTURE, RESERVATION_TIME_FIXTURE, ROOM_THEME_FIXTURE));
        // when
        boolean existsTrue = reservationDao.existsByDateTime(DATE_FIXTURE, 1L);
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
        long timeId = inMemoryReservationTimeDb.insert(RESERVATION_TIME_FIXTURE);
        ReservationTime reservationTime = inMemoryReservationTimeDb.selectById(timeId);
        long roomThemeId = inMemoryRoomThemeDb.insert(ROOM_THEME_FIXTURE);
        RoomTheme roomTheme = inMemoryRoomThemeDb.selectById(roomThemeId);
        // when
        reservationDao.save(new Reservation(
                new Name("aa"), DATE_FIXTURE, reservationTime, roomTheme));
        // then
        assertThat(reservationDao.findAll()).hasSize(1);
    }

    @DisplayName("해당 id의 예약을 삭제한다.")
    @Test
    void deleteById() {
        // given
        long timeId = inMemoryReservationTimeDb.insert(RESERVATION_TIME_FIXTURE);
        ReservationTime reservationTime = inMemoryReservationTimeDb.selectById(timeId);
        long roomThemeId = inMemoryRoomThemeDb.insert(ROOM_THEME_FIXTURE);
        RoomTheme roomTheme = inMemoryRoomThemeDb.selectById(roomThemeId);
        Reservation savedReservation = reservationDao.save(
                new Reservation(new Name("aa"), DATE_FIXTURE, reservationTime, roomTheme));
        // when
        reservationDao.deleteById(savedReservation.getId());
        // then
        assertThat(reservationDao.findAll()).isEmpty();
    }

    @DisplayName("삭제 대상이 존재하면 true를 반환한다.")
    @Test
    void returnTrueWhenDeleted() {
        // given
        long timeId = inMemoryReservationTimeDb.insert(RESERVATION_TIME_FIXTURE);
        ReservationTime reservationTime = inMemoryReservationTimeDb.selectById(timeId);
        long roomThemeId = inMemoryRoomThemeDb.insert(ROOM_THEME_FIXTURE);
        RoomTheme roomTheme = inMemoryRoomThemeDb.selectById(roomThemeId);
        Reservation savedReservation = reservationDao.save(
                new Reservation(new Name("aa"), DATE_FIXTURE, reservationTime, roomTheme));
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
