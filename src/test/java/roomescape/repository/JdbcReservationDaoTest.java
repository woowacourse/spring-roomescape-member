package roomescape.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import io.restassured.RestAssured;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class JdbcReservationDaoTest {

    @LocalServerPort
    private Integer port;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private ReservationDao reservationDao;
    private final RowMapper<Reservation> reservationRowMapper = (resultSet, rowNum) -> new Reservation(
            resultSet.getLong("reservation_id"),
            resultSet.getString("name"),
            resultSet.getDate("date").toLocalDate(),
            new ReservationTime(resultSet.getLong("time_id"), resultSet.getTime("time_value").toLocalTime()),
            new Theme(resultSet.getLong("theme_id"), resultSet.getString("theme_name"),
                    resultSet.getString("theme_description"), resultSet.getString("theme_thumbnail"))
    );

    @BeforeEach
    void setPort() {
        RestAssured.port = port;
    }

    @DisplayName("모든 예약을 조회한다.")
    @Test
    void findAll() {
        // given
        jdbcTemplate.update("INSERT INTO reservation_time (start_at) VALUES (?)", "10:00");
        jdbcTemplate.update("INSERT INTO theme (name, description, thumbnail) VALUES (?, ?, ?)", "이름", "설명", "썸네일");
        jdbcTemplate.update("INSERT INTO reservation (name, date, time_id, theme_id) VALUES (?, ?, ?, ?)", "커비",
                "2099-12-31", 1, 1);

        // when
        List<Reservation> reservations = reservationDao.findAll();

        // then
        assertAll(
                () -> assertThat(reservations.get(0).getId()).isEqualTo(1),
                () -> assertThat(reservations.get(0).getName()).isEqualTo("커비"),
                () -> assertThat(reservations.get(0).getDate()).isEqualTo(LocalDate.of(2099, 12, 31)),
                () -> assertThat(reservations.get(0).getTime().getId()).isEqualTo(1),
                () -> assertThat(reservations.get(0).getTime().getStartAt()).isEqualTo(LocalTime.of(10, 0)),
                () -> assertThat(reservations.get(0).getTheme().getId()).isEqualTo(1),
                () -> assertThat(reservations.get(0).getTheme().getName()).isEqualTo("이름"),
                () -> assertThat(reservations.get(0).getTheme().getDescription()).isEqualTo("설명"),
                () -> assertThat(reservations.get(0).getTheme().getThumbnail()).isEqualTo("썸네일")
        );
    }

    @DisplayName("해당 id의 예약을 조회한다.")
    @Test
    void findById() {
        // given
        jdbcTemplate.update("INSERT INTO reservation_time (start_at) VALUES (?)", "10:00");
        jdbcTemplate.update("INSERT INTO theme (name, description, thumbnail) VALUES (?, ?, ?)", "이름", "설명", "썸네일");
        jdbcTemplate.update("INSERT INTO reservation (name, date, time_id, theme_id) VALUES (?, ?, ?, ?)", "커비",
                "2099-12-31", 1, 1);

        // when
        Reservation reservation = reservationDao.findById(1);

        // then
        assertAll(
                () -> assertThat(reservation.getId()).isEqualTo(1),
                () -> assertThat(reservation.getName()).isEqualTo("커비"),
                () -> assertThat(reservation.getDate()).isEqualTo(LocalDate.of(2099, 12, 31)),
                () -> assertThat(reservation.getTime().getId()).isEqualTo(1),
                () -> assertThat(reservation.getTheme().getId()).isEqualTo(1)
        );
    }

    @DisplayName("날짜,시간,테마에 맞는 예약이 있는지 확인한다.")
    @Test
    void existByDateAndTimeAndTheme() {
        // given
        jdbcTemplate.update("INSERT INTO reservation_time (start_at) VALUES (?)", "10:00");
        jdbcTemplate.update("INSERT INTO theme (name, description, thumbnail) VALUES (?, ?, ?)", "이름", "설명", "썸네일");
        jdbcTemplate.update("INSERT INTO reservation (name, date, time_id, theme_id) VALUES (?, ?, ?, ?)", "커비",
                "2024-05-01", 1, 1);

        // when
        boolean existByDateTimeTheme = reservationDao.existByDateAndTimeAndTheme(LocalDate.of(2024, 5, 1), 1L, 1L);

        // then
        assertThat(existByDateTimeTheme).isTrue();
    }


    @DisplayName("예약을 저장한다.")
    @Test
    void save() {
        // given && when
        jdbcTemplate.update("INSERT INTO reservation_time (start_at) VALUES (?)", "10:00");
        jdbcTemplate.update("INSERT INTO theme (name, description, thumbnail) VALUES (?, ?, ?)", "이름", "설명", "썸네일");
        long id = reservationDao.save(new Reservation(null, "커비", LocalDate.of(2099, 12, 31),
                new ReservationTime(1L, LocalTime.of(10, 0)),
                new Theme(1L, "이름", "설명", "썸네일")));

        List<Reservation> reservations = jdbcTemplate.query("""
                SELECT
                r.id as reservation_id,
                r.name,
                r.date,
                t.id as time_id,
                t.start_at as time_value,
                th.id as theme_id,
                th.name as theme_name,
                th.description as theme_description,
                th.thumbnail as theme_thumbnail
                FROM reservation as r
                INNER JOIN reservation_time as t on r.time_id = t.id
                INNER JOIN theme as th on r.theme_id = th.id
                """, reservationRowMapper);

        // then
        assertAll(
                () -> assertThat(id).isEqualTo(1),
                () -> assertThat(reservations.get(0).getId()).isEqualTo(1),
                () -> assertThat(reservations.get(0).getName()).isEqualTo("커비"),
                () -> assertThat(reservations.get(0).getDate()).isEqualTo(LocalDate.of(2099, 12, 31)),
                () -> assertThat(reservations.get(0).getTime().getId()).isEqualTo(1),
                () -> assertThat(reservations.get(0).getTheme().getId()).isEqualTo(1)
        );

    }


    @DisplayName("아이디에 해당하는 예약을 삭제한다.")
    @Test
    void deleteById() {
        // given
        jdbcTemplate.update("INSERT INTO reservation_time (start_at) VALUES (?)", "10:00");
        jdbcTemplate.update("INSERT INTO theme (name, description, thumbnail) VALUES (?, ?, ?)", "이름", "설명", "썸네일");
        jdbcTemplate.update("INSERT INTO reservation (name, date, time_id, theme_id) VALUES (?, ?, ?, ?)", "커비",
                "2099-12-31", 1, 1);

        // when
        reservationDao.deleteById(1L);

        List<Reservation> reservations = jdbcTemplate.query("SELECT * FROM reservation", reservationRowMapper);

        // then
        assertThat(reservations).isEmpty();
    }

    @DisplayName("날짜와 테마별 시간들을 조회한다.")
    @Test
    void findTimeIdsByDateAndThemeId() {
        // given
        jdbcTemplate.update("INSERT INTO reservation_time (start_at) VALUES (?)", "10:00");
        jdbcTemplate.update("INSERT INTO theme (name, description, thumbnail) VALUES (?, ?, ?)", "이름", "설명", "썸네일");
        jdbcTemplate.update("INSERT INTO reservation (name, date, time_id, theme_id) VALUES (?, ?, ?, ?)", "커비",
                "2099-12-31", 1, 1);

        // when
        List<Long> times = reservationDao.findTimeIdsByDateAndThemeId(LocalDate.of(2099, 12, 31), 1L);

        // then
        assertThat(times).containsExactly(1L);
    }
}
