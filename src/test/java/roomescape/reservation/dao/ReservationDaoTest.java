package roomescape.reservation.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import roomescape.reservation.domain.Reservation;
import roomescape.reservationTime.dao.ReservationTimeDao;
import roomescape.reservationTime.domain.ReservationTime;
import roomescape.theme.dao.ThemeDao;
import roomescape.theme.domain.Theme;

@JdbcTest(properties = "spring.sql.init.mode=never")
@Import({ReservationDao.class, ReservationTimeDao.class, ThemeDao.class})
class ReservationDaoTest {

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    @Autowired
    private ReservationDao reservationDao;
    @Autowired
    private ReservationTimeDao reservationTimeDao;
    @Autowired
    private ThemeDao themeDao;

    @BeforeEach
    void setUp() {
        JdbcTemplate jdbcTemplate = namedParameterJdbcTemplate.getJdbcTemplate();

        jdbcTemplate.execute("DROP TABLE IF EXISTS reservation");
        jdbcTemplate.execute("DROP TABLE IF EXISTS reservation_time");
        jdbcTemplate.execute("DROP TABLE IF EXISTS theme");

        jdbcTemplate.execute("""
                    CREATE TABLE reservation_time (
                        id BIGINT NOT NULL AUTO_INCREMENT,
                        start_at VARCHAR(255) NOT NULL,
                        PRIMARY KEY (id)
                    );
                """);

        jdbcTemplate.execute("""
                    CREATE TABLE theme (
                        id BIGINT NOT NULL AUTO_INCREMENT,
                        name VARCHAR(255) NOT NULL,
                        description VARCHAR(255) NOT NULL,
                        thumbnail VARCHAR(255) NOT NULL,
                        PRIMARY KEY (id)
                    );
                """);

        jdbcTemplate.execute("""
                    CREATE TABLE reservation (
                        id BIGINT NOT NULL AUTO_INCREMENT,
                        name VARCHAR(255) NOT NULL,
                        date DATE NOT NULL,
                        time_id BIGINT,
                        theme_id BIGINT,
                        PRIMARY KEY (id),
                        FOREIGN KEY (time_id) REFERENCES reservation_time (id),
                        FOREIGN KEY (theme_id) REFERENCES theme (id)
                    );
                """);

        String insertSqlReservationTime = "INSERT INTO reservation_time(start_at) VALUES (:start_at)";
        namedParameterJdbcTemplate.update(
                insertSqlReservationTime,
                Map.of(
                        "start_at", "10:00"
                )
        );

        String insertSqlReservationTime2 = "INSERT INTO reservation_time(start_at) VALUES (:start_at)";
        namedParameterJdbcTemplate.update(
                insertSqlReservationTime2,
                Map.of(
                        "start_at", "11:00"
                )
        );

        String insertSqlTheme = "INSERT INTO theme(name, description, thumbnail) VALUES (:name, :description, :thumbnail)";
        namedParameterJdbcTemplate.update(
                insertSqlTheme,
                Map.of(
                        "name", "방 탈출1",
                        "description", "공포 테마",
                        "thumbnail", "horror.jpg"
                )
        );

        String insertSqlReservation = "INSERT INTO reservation(name, date, time_id, theme_id) VALUES (:name, :date, :time_id, :theme_id)";
        namedParameterJdbcTemplate.update(
                insertSqlReservation,
                Map.of(
                        "name", "홍길동",
                        "date", "2025-05-01",
                        "time_id", 1L,
                        "theme_id", 1L
                )
        );
    }

    @DisplayName("예약 내역을 조회하는 기능을 구현한다")
    @Test
    void findAll() {
        List<Reservation> reservations = reservationDao.findAll();

        assertThat(reservations).hasSize(1);
    }

    @DisplayName("예약 내역을 아이디로 조회하는 기능을 구현한다")
    @Test
    void findById() {
        Reservation reservation = reservationDao.findById(1L).get();

        assertThat(reservation.getId()).isEqualTo(1L);
    }

    @DisplayName("예약 내역을 추가하는 기능을 구현한다")
    @Test
    void add() {
        Reservation reservation = new Reservation(
                2L,
                "곰돌이",
                LocalDate.parse("2025-05-01"),
                new ReservationTime(2L, LocalTime.parse("11:00")),
                new Theme(1L, "방 탈출1", "공포 테마", "horror.jpg")
        );

        reservationDao.add(reservation);

        assertThat(reservationDao.findAll()).hasSize(2);
    }

    @DisplayName("예약 내역을 삭제하는 기능을 구현한다")
    @Test
    void deleteById() {
        reservationDao.deleteById(1L);

        assertThat(reservationDao.findAll()).hasSize(0);
    }
}
