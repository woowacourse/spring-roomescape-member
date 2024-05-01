package roomescape.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDate;
import java.time.LocalTime;
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
        boolean existsFalse = reservationDao.existsByDateTime(LocalDate.of(9999, 12, 12), 1L);
        reservationDao.save(new Reservation(new Name("asd"),
                LocalDate.parse("9999-12-12"),
                new ReservationTime(1L, LocalTime.of(10, 0)),
                new RoomTheme(1L, "레벨 2 탈출", "우테코 레벨2를 탈출하는 내용입니다.",
                        "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg")));
        boolean existsTrue = reservationDao.existsByDateTime(LocalDate.of(9999, 12, 12), 1L);

        assertAll(
                () -> assertThat(existsFalse).isFalse(),
                () -> assertThat(existsTrue).isTrue()
        );
    }

    @DisplayName("예약을 저장한다.")
    @Test
    void save() {
        //given
        long timeId = inMemoryReservationTimeDb.insert(LocalTime.of(10, 0));
        ReservationTime reservationTime = inMemoryReservationTimeDb.selectById(timeId);
        long roomThemeId = inMemoryRoomThemeDb.insert(new RoomTheme("레벨 2 탈출", "우테코 레벨2를 탈출하는 내용입니다.",
                "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg"));
        RoomTheme roomTheme = inMemoryRoomThemeDb.selectById(roomThemeId);
        //when
        reservationDao.save(new Reservation(new Name("aa"), LocalDate.parse("9999-10-10"), reservationTime, roomTheme));
        //then
        assertThat(reservationDao.findAll()).hasSize(1);
    }

    @DisplayName("해당 id의 예약을 삭제한다.")
    @Test
    void deleteById() {
        //given
        long timeId = inMemoryReservationTimeDb.insert(LocalTime.of(10, 0));
        ReservationTime reservationTime = inMemoryReservationTimeDb.selectById(timeId);
        long roomThemeId = inMemoryRoomThemeDb.insert(new RoomTheme("레벨 2 탈출", "우테코 레벨2를 탈출하는 내용입니다.",
                "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg"));
        RoomTheme roomTheme = inMemoryRoomThemeDb.selectById(roomThemeId);
        Reservation savedReservation = reservationDao.save(
                new Reservation(new Name("aa"), LocalDate.parse("9999-10-10"), reservationTime, roomTheme));
        //when
        reservationDao.deleteById(savedReservation.getId());
        //then
        assertThat(reservationDao.findAll()).isEmpty();
    }

    @DisplayName("삭제 대상이 존재하면 true를 반환한다.")
    @Test
    void returnTrueWhenDeleted() {
        //given
        long timeId = inMemoryReservationTimeDb.insert(LocalTime.of(10, 0));
        ReservationTime reservationTime = inMemoryReservationTimeDb.selectById(timeId);
        long roomThemeId = inMemoryRoomThemeDb.insert(new RoomTheme("레벨 2 탈출", "우테코 레벨2를 탈출하는 내용입니다.",
                "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg"));
        RoomTheme roomTheme = inMemoryRoomThemeDb.selectById(roomThemeId);
        Reservation savedReservation = reservationDao.save(
                new Reservation(new Name("aa"), LocalDate.parse("9999-10-10"), reservationTime, roomTheme));
        //when
        boolean deleted = reservationDao.deleteById(savedReservation.getId());
        //then
        assertThat(deleted).isTrue();
    }

    @DisplayName("삭제 대상이 존재하지 않으면 false를 반환한다.")
    @Test
    void returnFalseWhenNotDeleted() {
        //when
        boolean deleted = reservationDao.deleteById(1L);
        //then
        assertThat(deleted).isFalse();
    }
}
