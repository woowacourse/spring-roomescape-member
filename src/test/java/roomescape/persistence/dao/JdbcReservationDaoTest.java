package roomescape.persistence.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import roomescape.business.domain.PlayTime;
import roomescape.business.domain.Reservation;
import roomescape.business.domain.Theme;

@JdbcTest
class JdbcReservationDaoTest {

    private final ReservationDao reservationDao;
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public JdbcReservationDaoTest(final JdbcTemplate jdbcTemplate) {
        this.reservationDao = new JdbcReservationDao(jdbcTemplate);
        this.jdbcTemplate = jdbcTemplate;
    }

    @BeforeEach
    void setUp() {
        // 테스트를 위한 reservation_time, theme 데이터 삽입
        final String playTimeInsertSql = """
                INSERT INTO reservation_time(id, start_at)
                VALUES (1, '10:10')
                """;
        jdbcTemplate.execute(playTimeInsertSql);
        final String themeInsertSql = """
                INSERT INTO theme(id, name, description, thumbnail)
                VALUES (1, '테마', '소개', '썸네일')
                """;
        jdbcTemplate.execute(themeInsertSql);
    }

    @Test
    @DisplayName("데이터 들어갔는지 확인")
    void insertCheck() {
        final String sql = """
                SELECT start_at
                FROM reservation_time
                WHERE id = 1
                """;
        final String result = jdbcTemplate.queryForObject(sql, String.class);
        assertThat(result).isEqualTo("10:10");
    }

    @Test
    @DisplayName("데이터베이스에 방탈출 예약을 저장하고 조회하여 확인한다")
    void insertAndFindById() {
        // given
        final Reservation reservation = new Reservation(
                "kim",
                LocalDate.of(2999, 1, 1),
                new PlayTime(1L),
                new Theme(1L)
        );

        // when
        final Long id = reservationDao.insert(reservation);

        // then
        final Optional<Reservation> findReservation = reservationDao.findById(id);
        assertAll(
                () -> assertThat(findReservation).isPresent(),
                () -> assertThat(findReservation.get().getId()).isEqualTo(id),
                () -> assertThat(findReservation.get().getName()).isEqualTo(reservation.getName())
        );
    }

    @Test
    @DisplayName("데이터베이스에서 id를 통해 방탈출 예약을 삭제한다")
    void deleteById() {
        // given
        final Reservation reservationDay1 = new Reservation(
                "kim",
                LocalDate.of(2999, 1, 1),
                new PlayTime(1L),
                new Theme(1L)
        );
        final Long id = reservationDao.insert(reservationDay1);

        // when
        final boolean isDeleted = reservationDao.deleteById(id);

        // then
        assertAll(
                () -> assertThat(isDeleted).isTrue(),
                () -> assertThat(reservationDao.findById(reservationDay1.getId())).isEmpty()
        );
    }

    @Test
    @DisplayName("데이터베이스의 모든 방탈출 예약을 조회한다")
    void findAll() {
        // given
        final Reservation reservationDay1 = new Reservation(
                "kim",
                LocalDate.of(2999, 1, 1),
                new PlayTime(1L),
                new Theme(1L)
        );
        final Reservation reservationDay2 = new Reservation(
                "kim",
                LocalDate.of(2999, 1, 2),
                new PlayTime(1L),
                new Theme(1L)
        );
        reservationDao.insert(reservationDay1);
        reservationDao.insert(reservationDay2);

        // when
        final List<Reservation> reservations = reservationDao.findAll();

        // then
        assertThat(reservations).hasSize(2);
    }

    @Test
    @DisplayName("데이터베이스에서 id를 통해 방탈출 예약을 삭제할 때 대상이 없다면 false 반환한다")
    void deleteByIdWhenNotExist() {
        // given
        final Long notExistId = 999L;

        // when
        final boolean isDeleted = reservationDao.deleteById(notExistId);

        // then
        assertThat(isDeleted).isFalse();
    }

    @Test
    @DisplayName("데이터베이스에서 날짜와 시간과 테마가 동일한 대상이 존재하는지 확인한다")
    void existsByDateAndTime() {
        // given
        final LocalDate validDate = LocalDate.of(2999, 1, 1);
        final LocalDate invalidDate = LocalDate.of(2999, 1, 2);
        final Reservation reservationDay1 = new Reservation(
                "kim",
                validDate,
                new PlayTime(1L),
                new Theme(1L)
        );
        reservationDao.insert(reservationDay1);

        // when
        final boolean existReservation = reservationDao.existsByDateAndTimeIdAndThemeId(
                validDate, 1L, 1L);
        final boolean notExistReservation = reservationDao.existsByDateAndTimeIdAndThemeId(
                invalidDate, 1L, 1L);

        // then
        assertAll(
                () -> assertThat(existReservation).isTrue(),
                () -> assertThat(notExistReservation).isFalse()
        );
    }
}
