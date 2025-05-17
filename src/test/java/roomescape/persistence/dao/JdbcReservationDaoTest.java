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
import roomescape.business.domain.Role;
import roomescape.business.domain.Theme;
import roomescape.business.domain.User;
import roomescape.persistence.entity.PlayTimeEntity;
import roomescape.persistence.entity.ReservationEntity;
import roomescape.persistence.entity.ThemeEntity;
import roomescape.persistence.entity.UserEntity;
import roomescape.presentation.dto.reservation.ReservationAvailableTimeResponse;

@JdbcTest
class JdbcReservationDaoTest {

    private ReservationDao reservationDao;

    private final JdbcTemplate jdbcTemplate;
    private final User userFixture = User.createWithId(1L, "hotteok", "hoho", "qwe123", Role.USER);
    private final PlayTime playTimeFixture = PlayTime.createWithId(1L, LocalTime.of(10, 10));
    private final Theme themeFixture = Theme.createWithId(1L, "테마", "소개", "썸네일");

    @Autowired
    public JdbcReservationDaoTest(final JdbcTemplate jdbcTemplate) {
        jdbcTemplate.execute("DROP TABLE IF EXISTS users CASCADE");
        jdbcTemplate.execute("DROP TABLE IF EXISTS reservation_time CASCADE");
        jdbcTemplate.execute("DROP TABLE IF EXISTS theme CASCADE");

        jdbcTemplate.execute("""
                CREATE TABLE users
                  (
                      id   SERIAL,
                      name VARCHAR(255) NOT NULL,
                      email VARCHAR(255) NOT NULL UNIQUE,
                      password VARCHAR(255) NOT NULL,
                      role VARCHAR(255) NOT NULL,
                      PRIMARY KEY (id)
                  );
                """);
        jdbcTemplate.execute("""
                CREATE TABLE reservation_time
                (
                    id SERIAL,
                    start_at VARCHAR(255) NOT NULL,
                    PRIMARY KEY (id)
                );
                """);
        jdbcTemplate.execute("""
                CREATE TABLE theme
                (
                    id SERIAL,
                    name        VARCHAR(255) NOT NULL,
                    description VARCHAR(255) NOT NULL,
                    thumbnail VARCHAR(255) NOT NULL,
                    PRIMARY KEY (id)
                );
                """);
        jdbcTemplate.update("INSERT INTO USERS (name, email, password, role) values ('hotteok', 'hoho', 'qwe123', 'USER')");
        jdbcTemplate.update("INSERT INTO USERS (name, email, password, role) values ('saba', 'saba', 'qwe123', 'USER')");
        jdbcTemplate.update("INSERT INTO reservation_time (start_at) VALUES ('10:10')");
        jdbcTemplate.update("INSERT INTO theme (name, description, thumbnail) VALUES ('테마', '소개', '썸네일')");

        this.jdbcTemplate = jdbcTemplate;
    }

    @BeforeEach
    void setUp() {
        reservationDao = new JdbcReservationDao(jdbcTemplate);

        jdbcTemplate.execute("DROP TABLE IF EXISTS reservation CASCADE");
        jdbcTemplate.execute("""
                CREATE TABLE reservation (
                    id SERIAL PRIMARY KEY,
                    user_id BIGINT,
                    date VARCHAR(255) NOT NULL,
                    time_id BIGINT,
                    theme_id BIGINT,
                    FOREIGN KEY (user_id) REFERENCES users(id),
                    FOREIGN KEY (time_id) REFERENCES reservation_time(id),
                    FOREIGN KEY (theme_id) REFERENCES theme(id)
                );
                """);
    }

    @DisplayName("데이터베이스에 방탈출 예약을 저장한다.")
    @Test
    void save() {
        // given & when
        final Long id = reservationDao.save(new Reservation(
                userFixture,
                LocalDate.of(2025, 1, 1),
                playTimeFixture,
                themeFixture
        ));
        final ReservationEntity actual = jdbcTemplate.queryForObject("""
                SELECT
                r.id AS reservation_id,
                u.id AS user_id,
                u.name AS user_name,
                u.email AS user_email,
                u.password AS user_password,
                u.role AS user_role,
                r.date,
                rt.id AS time_id,
                rt.start_at AS time_value,
                t.id AS theme_id,
                t.name AS theme_name,
                t.description AS theme_description,
                t.thumbnail AS theme_thumbnail
                FROM reservation AS r
                INNER JOIN users AS u 
                ON r.user_id = u.id
                INNER JOIN reservation_time AS rt
                ON r.time_id = rt.id 
                INNER JOIN theme AS t 
                ON r.theme_id = t.id
                WHERE r.id = ?
                """, ReservationEntity.getDefaultRowMapper(), id
        );

        // then
        assertThat(actual).isEqualTo(new ReservationEntity(
                1L,
                UserEntity.from(userFixture),
                "2025-01-01",
                PlayTimeEntity.from(playTimeFixture),
                ThemeEntity.from(themeFixture))
        );
    }

    @DisplayName("데이터베이스에서 모든 방탈출 예약을 조회한다.")
    @Test
    void findAll() {
        // given
        jdbcTemplate.update("INSERT INTO RESERVATION (user_id, date, time_id, theme_id) values (1, '2025-01-01', 1, 1)");
        jdbcTemplate.update("INSERT INTO RESERVATION (user_id, date, time_id, theme_id) values (1, '2025-01-02', 1, 1)");

        // when
        final List<Reservation> actual = reservationDao.findAll();

        // then
        assertThat(actual).containsExactlyInAnyOrder(
                Reservation.createWithId(1L, userFixture, LocalDate.of(2025, 1, 1), playTimeFixture, themeFixture),
                Reservation.createWithId(2L, userFixture, LocalDate.of(2025, 1, 2), playTimeFixture, themeFixture)
        );
    }

    @DisplayName("데이터베이스에서 방탈출 예약을 삭제한다.")
    @Test
    void remove() {
        // given
        jdbcTemplate.update("INSERT INTO RESERVATION (user_id, date, time_id, theme_id) values (1, '2025-01-01', 1, 1)");

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

    @DisplayName("데이터베이스에서 해당 날짜에 해당하는 방탈출 예약이 존재하는지 확인한다.")
    @Test
    void existsByDate() {
        // given
        jdbcTemplate.update("INSERT INTO RESERVATION (user_id, date, time_id, theme_id) values (1, '2025-01-01', 1, 1)");
        final LocalDate validDate = LocalDate.of(2025, 1, 1);
        final LocalDate invalidDate = LocalDate.of(2025, 1, 2);

        // when & then
        assertAll(
                () -> assertThat(reservationDao.existsByDateAndTimeAndTheme(validDate, playTimeFixture, themeFixture)).isTrue(),
                () -> assertThat(reservationDao.existsByDateAndTimeAndTheme(invalidDate, playTimeFixture, themeFixture)).isFalse()
        );
    }

    @DisplayName("데이터베이스에서 해당 시간에 해당하는 방탈출 예약이 존재하는지 확인한다.")
    @Test
    void existsByTime() {
        // given
        jdbcTemplate.update("INSERT INTO RESERVATION (user_id, date, time_id, theme_id) values (1, '2025-01-01', 1, 1)");
        final PlayTime validTime = playTimeFixture;
        final PlayTime invalidTime = new PlayTime(LocalTime.of(10, 20));

        // when & then
        assertAll(
                () -> assertThat(reservationDao.existsByDateAndTimeAndTheme(
                        LocalDate.of(2025, 1, 1),
                        validTime,
                        themeFixture)).isTrue(),
                () -> assertThat(reservationDao.existsByDateAndTimeAndTheme(
                        LocalDate.of(2025, 1, 1),
                        invalidTime,
                        themeFixture)).isFalse()
        );
    }

    @DisplayName("데이터베이스에서 해당 테마에 해당하는 방탈출 예약이 존재하는지 확인한다.")
    @Test
    void existsByTheme() {
        // given
        jdbcTemplate.update("INSERT INTO RESERVATION (user_id, date, time_id, theme_id) values (1, '2025-01-01', 1, 1)");
        final Theme validTheme = themeFixture;
        final Theme invalidTheme = new Theme("더미", "더미", "더미");

        // when & then
        assertAll(
                () -> assertThat(reservationDao.existsByDateAndTimeAndTheme(
                        LocalDate.of(2025, 1, 1),
                        playTimeFixture,
                        validTheme)).isTrue(),
                () -> assertThat(reservationDao.existsByDateAndTimeAndTheme(
                        LocalDate.of(2025, 1, 1),
                        playTimeFixture,
                        invalidTheme)).isFalse()
        );
    }

    @DisplayName("데이터베이스에서 모든 시간에 대해 해당 날짜와 테마에 예약 여부를 조회한다.")
    @Test
    void findAvailableTimesByDateAndTheme() {
        // given
        jdbcTemplate.update("INSERT INTO reservation_time (start_at) VALUES ('10:20')");
        jdbcTemplate.update("INSERT INTO RESERVATION (user_id, date, time_id, theme_id) values (1, '2025-01-01', 1, 1)");

        // when
        final List<ReservationAvailableTimeResponse> actual =
                reservationDao.findAvailableTimesByDateAndTheme(LocalDate.of(2025, 1, 1), themeFixture);

        // then
        assertThat(actual).containsExactly(
                new ReservationAvailableTimeResponse("10:10", 1L, true),
                new ReservationAvailableTimeResponse("10:20", 2L, false)
        );
    }
}
