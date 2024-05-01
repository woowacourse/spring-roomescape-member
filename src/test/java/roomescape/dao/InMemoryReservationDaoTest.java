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
import roomescape.domain.Name;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;

class InMemoryReservationDaoTest {
    private InMemoryReservationTimeDb inMemoryReservationTimeDb;
    private ReservationDao reservationDao;

    @BeforeEach
    void setUp() {
        inMemoryReservationTimeDb = new InMemoryReservationTimeDb();
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
        reservationDao.save(new Reservation(new Name("asd"), LocalDate.parse("9999-12-12"), new ReservationTime(1L,
                LocalTime.of(10, 0))));
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
        inMemoryReservationTimeDb.insert(LocalTime.of(10, 0));
        ReservationTime reservationTime = inMemoryReservationTimeDb.selectById(1L);
        //when
        reservationDao.save(new Reservation(new Name("aa"), LocalDate.parse("9999-10-10"), reservationTime));
        //then
        assertThat(reservationDao.findAll()).hasSize(1);
    }

    @DisplayName("해당 id의 예약을 삭제한다.")
    @Test
    void deleteById() {
        //given
        inMemoryReservationTimeDb.insert(LocalTime.of(10, 0));
        ReservationTime reservationTime = inMemoryReservationTimeDb.selectById(1L);
        reservationDao.save(new Reservation(new Name("aa"), LocalDate.parse("9999-10-10"), reservationTime));
        //when
        reservationDao.deleteById(1L);
        //then
        assertThat(reservationDao.findAll()).hasSize(0);
    }

    @DisplayName("삭제 대상이 존재하면 true를 반환한다.")
    @Test
    void returnTrueWhenDeleted() {
        //given
        inMemoryReservationTimeDb.insert(LocalTime.of(10, 0));
        ReservationTime reservationTime = inMemoryReservationTimeDb.selectById(1L);
        reservationDao.save(new Reservation(new Name("aa"), LocalDate.parse("9999-10-10"), reservationTime));
        //when
        boolean deleted = reservationDao.deleteById(1L);
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
