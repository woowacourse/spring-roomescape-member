package roomescape.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static roomescape.TestFixture.DATE_FIXTURE;
import static roomescape.TestFixture.RESERVATION_TIME_FIXTURE;
import static roomescape.TestFixture.ROOM_THEME_FIXTURE;
import static roomescape.TestFixture.TIME_FIXTURE;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.dao.EmptyResultDataAccessException;
import roomescape.console.dao.InMemoryReservationTimeDao;
import roomescape.console.db.InMemoryReservationDb;
import roomescape.console.db.InMemoryReservationTimeDb;
import roomescape.domain.Name;
import roomescape.domain.Reservation;

class InMemoryReservationTimeDaoTest {
    private InMemoryReservationDb inMemoryReservationDb;
    private ReservationTimeDao reservationTimeDao;

    @BeforeEach
    void setUp() {
        inMemoryReservationDb = new InMemoryReservationDb();
        reservationTimeDao = new InMemoryReservationTimeDao(
                inMemoryReservationDb, new InMemoryReservationTimeDb());
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
        reservationTimeDao.save(RESERVATION_TIME_FIXTURE);
        // then
        assertThat(reservationTimeDao.findById(1L).getStartAt()).isEqualTo(TIME_FIXTURE);
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
        reservationTimeDao.save(RESERVATION_TIME_FIXTURE);
        // when
        reservationTimeDao.deleteById(1L);
        // then
        assertThat(reservationTimeDao.findAll()).isEmpty();
    }

    @DisplayName("해당 id의 예약 시간을 삭제하는 경우, 그 id를 참조하는 예약도 삭제한다.")
    @Test
    void deleteByIdDeletesReservationAlso() {
        // given
        reservationTimeDao.save(RESERVATION_TIME_FIXTURE);
        inMemoryReservationDb.insert(new Reservation(new Name("aa"), DATE_FIXTURE,
                RESERVATION_TIME_FIXTURE, ROOM_THEME_FIXTURE));
        // when
        reservationTimeDao.deleteById(1L);
        // then
        assertThat(inMemoryReservationDb.selectAll()).isEmpty();
    }

    @DisplayName("삭제 대상이 존재하면 true를 반환한다.")
    @Test
    void returnTrueWhenDeleted() {
        // given
        reservationTimeDao.save(RESERVATION_TIME_FIXTURE);
        // when
        boolean deleted = reservationTimeDao.deleteById(1L);
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
