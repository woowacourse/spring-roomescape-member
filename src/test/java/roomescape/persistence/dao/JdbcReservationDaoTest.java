package roomescape.persistence.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import roomescape.business.domain.PlayTime;
import roomescape.business.domain.Reservation;
import roomescape.persistence.entity.PlayTimeEntity;
import roomescape.persistence.entity.ReservationEntity;

@JdbcTest
class JdbcReservationDaoTest {

    private ReservationDao reservationDao;

    private final JdbcTemplate jdbcTemplate;
    private final PlayTime playTimeFixture = PlayTime.createWithId(1L, LocalTime.of(10, 10));

    @Autowired
    public JdbcReservationDaoTest(final JdbcTemplate jdbcTemplate) {
        jdbcTemplate.execute("""
                CREATE TABLE IF NOT EXISTS reservation_time
                (
                    id SERIAL,
                    start_at VARCHAR(255) NOT NULL,
                    PRIMARY KEY (id)
                );
                """);
        jdbcTemplate.update("INSERT INTO reservation_time (start_at) VALUES ('10:10')");

        this.jdbcTemplate = jdbcTemplate;
    }

    @BeforeEach
    void setUp() {
        reservationDao = new JdbcReservationDao(jdbcTemplate);

        jdbcTemplate.execute("DROP TABLE IF EXISTS reservation CASCADE");
        jdbcTemplate.execute("""
                CREATE TABLE reservation
                (
                    id SERIAL,
                    name VARCHAR(255) NOT NULL,
                    date VARCHAR(255) NOT NULL,
                    time_id BIGINT,
                    PRIMARY KEY (id),
                    FOREIGN KEY (time_id) REFERENCES reservation_time (id)
                );
                """);
    }

    @DisplayName("데이터베이스에 방탈출 예약을 저장한다.")
    @Test
    void save() {
        // given & when
        final Long id = reservationDao.save(new Reservation(
                "hotteok",
                LocalDate.of(2025, 1, 1),
                playTimeFixture
        ));
        final ReservationEntity actual = jdbcTemplate.queryForObject("""
                SELECT
                    r.id as reservation_id,
                    r.name,
                    r.date,
                    t.id as time_id,
                    t.start_at as time_value
                    FROM reservation as r
                    inner join reservation_time as t
                    on r.time_id = t.id
                    WHERE r.id = ?
                """, ReservationEntity.getDefaultRowMapper(), id
        );

        // then
        assertThat(actual).isEqualTo(new ReservationEntity(
                1L,
                "hotteok",
                "2025-01-01",
                PlayTimeEntity.from(playTimeFixture))
        );
    }

    @DisplayName("데이터베이스에서 모든 방탈출 예약을 조회한다.")
    @Test
    void findAll() {
        // given
        jdbcTemplate.update("INSERT INTO RESERVATION (name, date, time_id) values ('hotteok', '2025-01-01', 1)");
        jdbcTemplate.update("INSERT INTO RESERVATION (name, date, time_id) values ('hotteok', '2025-01-02', 1)");

        // when
        final List<Reservation> actual = reservationDao.findAll();

        // then
        assertThat(actual).containsExactly(
                Reservation.createWithId(1L, "hotteok", LocalDate.of(2025, 1, 1), playTimeFixture),
                Reservation.createWithId(2L, "hotteok", LocalDate.of(2025, 1, 2), playTimeFixture)
        );
    }

    @DisplayName("데이터베이스에서 방탈출 예약을 삭제한다.")
    @Test
    void remove() {
        // given
        jdbcTemplate.update("INSERT INTO RESERVATION (name, date, time_id) values ('hotteok', '2025-01-01', 1)");

        // when
        final boolean flag = reservationDao.remove(1L);
        final List<Reservation> reservations = reservationDao.findAll();

        // then
        assertAll(
                () -> assertThat(flag).isTrue(),
                () -> assertThat(reservations).isEmpty()
        );
    }

    @DisplayName("해당하는 방탈출 예약이 없다면 0을 반환한다.")
    @Test
    void removeNotExistsReservation() {
        // given & when
        final boolean flag = reservationDao.remove(1L);

        // then
        assertThat(flag).isFalse();
    }

    @DisplayName("데이터베이스에서 해당 방탈출 예약이 존재하는지 확인한다.")
    @Test
    void existsByDateAndTime() {
        // given
        jdbcTemplate.update("INSERT INTO RESERVATION (name, date, time_id) values ('hotteok', '2025-01-01', 1)");
        final LocalDate validDate = LocalDate.of(2025, 1, 1);
        final LocalDate invalidDate = LocalDate.of(2025, 1, 2);

        // when & then
        assertAll(
                () -> assertThat(reservationDao.existsByDateAndTime(validDate, playTimeFixture)).isTrue(),
                () -> assertThat(reservationDao.existsByDateAndTime(invalidDate, playTimeFixture)).isFalse()
        );
    }
}
