package roomescape.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;

@JdbcTest
@Sql(scripts = "/truncate.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class ReservationDaoTest {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    private ReservationDao reservationDao;

    @BeforeEach
    void setUp() {
        reservationDao = new ReservationDao(jdbcTemplate);
    }

    @DisplayName("모든 예약을 읽을 수 있다.")
    @Test
    void readReservationsTest() {
        jdbcTemplate.update("INSERT INTO reservation_time(start_at) VALUES (?)", "19:00");
        jdbcTemplate.update(
                "INSERT INTO theme (name, description, thumbnail) values (?, ?, ?)",
                "레벨2 탈출", "레벨2 탈출하기", "https://img.jpg");
        jdbcTemplate.update(
                "INSERT INTO reservation (name, date, time_id, theme_id) values (?, ?, ?, ?)",
                "브라운", "2024-08-15", 1, 1);
        jdbcTemplate.update(
                "INSERT INTO reservation (name, date, time_id, theme_id) values (?, ?, ?, ?)",
                "브리", "2024-08-20", 1, 1);
        List<Reservation> expected = List.of(
                new Reservation(
                        1L, "브라운", LocalDate.of(2024, 8, 15),
                        new ReservationTime(1L, LocalTime.of(19, 0)),
                        new Theme(1L, "레벨2 탈출", "레벨2 탈출하기", "https://img.jpg")),
                new Reservation(
                        2L, "브리", LocalDate.of(2024, 8, 20),
                        new ReservationTime(1L, LocalTime.of(19, 0)),
                        new Theme(1L, "레벨2 탈출", "레벨2 탈출하기", "https://img.jpg")));

        List<Reservation> actual = reservationDao.readReservations();

        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("해당 날짜, 시간, 테마의 예약이 있는지 알 수 있다.")
    @Test
    void isExistReservationByDateAndTimeIdAndThemeIdTest() {
        jdbcTemplate.update("INSERT INTO reservation_time(start_at) VALUES (?)", "19:00");
        jdbcTemplate.update(
                "INSERT INTO theme (name, description, thumbnail) values (?, ?, ?)",
                "레벨2 탈출", "레벨2 탈출하기", "https://img.jpg");
        jdbcTemplate.update(
                "INSERT INTO reservation (name, date, time_id, theme_id) values (?, ?, ?, ?)",
                "브라운", "2024-08-15", 1, 1);

        assertThat(reservationDao.isExistReservationByDateAndTimeIdAndThemeId(
                        LocalDate.of(2024, 8, 15), 1L, 1L)).isTrue();
        assertThat(reservationDao.isExistReservationByDateAndTimeIdAndThemeId(
                        LocalDate.of(2024, 8, 16), 1L, 1L)).isFalse();
        assertThat(reservationDao.isExistReservationByDateAndTimeIdAndThemeId(
                        LocalDate.of(2024, 8, 15), 2L, 1L)).isFalse();
    }

    @DisplayName("예약을 생성할 수 있다.")
    @Test
    void createReservationTest() {
        jdbcTemplate.update("INSERT INTO reservation_time(start_at) VALUES (?)", "19:00");
        jdbcTemplate.update(
                "INSERT INTO theme (name, description, thumbnail) values (?, ?, ?)",
                "레벨2 탈출", "레벨2 탈출하기", "https://img.jpg");
        Reservation reservation = new Reservation(
                "브라운", LocalDate.of(2024, 8, 15),
                new ReservationTime(1L, LocalTime.of(19, 0)),
                new Theme(1L, "레벨2 탈출", "레벨2 탈출하기", "https://img.jpg"));
        Reservation expected = new Reservation(
                1L, "브라운", LocalDate.of(2024, 8, 15),
                new ReservationTime(1L, LocalTime.of(19, 0)),
                new Theme(1L, "레벨2 탈출", "레벨2 탈출하기", "https://img.jpg"));

        Reservation actual = reservationDao.createReservation(reservation);

        assertThat(actual).isEqualTo(expected);
        assertThat(countSavedReservation()).isEqualTo(1);
    }

    @DisplayName("예약을 삭제할 수 있다.")
    @Test
    void deleteReservationTest() {
        jdbcTemplate.update("INSERT INTO reservation_time(start_at) VALUES (?)", "19:00");
        jdbcTemplate.update(
                "INSERT INTO theme (name, description, thumbnail) values (?, ?, ?)",
                "레벨2 탈출", "레벨2 탈출하기", "https://img.jpg");
        jdbcTemplate.update(
                "INSERT INTO reservation (name, date, time_id, theme_id) values (?, ?, ?, ?)",
                "브라운", "2024-08-15", 1, 1);

        reservationDao.deleteReservation(1L);

        assertThat(countSavedReservation()).isZero();
    }

    private int countSavedReservation() {
        return jdbcTemplate.queryForObject("SELECT COUNT(1) FROM reservation", Integer.class);
    }
}
