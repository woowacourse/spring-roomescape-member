package roomescape.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.dao.EmptyResultDataAccessException;
import roomescape.console.dao.InMemoryReservationTimeDao;
import roomescape.console.db.InMemoryReservationDb;
import roomescape.console.db.InMemoryReservationTimeDb;
import roomescape.domain.ReservationTime;
import roomescape.domain.RoomTheme;

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
        //when
        reservationTimeDao.save(new ReservationTime(LocalTime.parse("10:00")));
        //then
        assertThat(reservationTimeDao.findAll()).hasSize(1);
    }

    @DisplayName("해당 id의 예약 시간을 보여준다.")
    @Test
    void findById() {
        //when
        reservationTimeDao.save(new ReservationTime(LocalTime.parse("10:00")));
        //then
        assertThat(reservationTimeDao.findById(1L).getStartAt()).isEqualTo(LocalTime.parse("10:00"));
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
        boolean existsFalse = reservationTimeDao.existByStartAt(LocalTime.of(10, 0));
        reservationTimeDao.save(new ReservationTime(LocalTime.parse("10:00")));
        boolean existsTrue = reservationTimeDao.existByStartAt(LocalTime.of(10, 0));

        assertAll(
                () -> assertThat(existsFalse).isFalse(),
                () -> assertThat(existsTrue).isTrue()
        );
    }

    @DisplayName("해당 id의 예약 시간을 삭제한다.")
    @Test
    void deleteById() {
        //given
        reservationTimeDao.save(new ReservationTime(LocalTime.parse("10:00")));
        //when
        reservationTimeDao.deleteById(1L);
        //then
        assertThat(reservationTimeDao.findAll()).isEmpty();
    }

    @DisplayName("해당 id의 예약 시간을 삭제하는 경우, 그 id를 참조하는 예약도 삭제한다.")
    @Test
    void deleteByIdDeletesReservationAlso() {
        reservationTimeDao.save(new ReservationTime(LocalTime.parse("10:00")));
        inMemoryReservationDb.insert("aa", "2024-10-11",
                new ReservationTime(1L, LocalTime.of(10, 0)),
                new RoomTheme(1L, "레벨 2 탈출", "우테코 레벨2를 탈출하는 내용입니다.",
                        "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg"));

        reservationTimeDao.deleteById(1L);

        assertThat(inMemoryReservationDb.selectAll()).isEmpty();
    }

    @DisplayName("삭제 대상이 존재하면 true를 반환한다.")
    @Test
    void returnTrueWhenDeleted() {
        //given
        reservationTimeDao.save(new ReservationTime(LocalTime.parse("10:00")));
        //when
        boolean deleted = reservationTimeDao.deleteById(1L);
        //then
        assertThat(deleted).isTrue();
    }

    @DisplayName("삭제 대상이 존재하지 않으면 false를 반환한다.")
    @Test
    void returnFalseWhenNotDeleted() {
        //when
        boolean deleted = reservationTimeDao.deleteById(1L);
        //then
        assertThat(deleted).isFalse();
    }
}
