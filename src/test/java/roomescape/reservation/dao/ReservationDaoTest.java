package roomescape.reservation.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
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
import org.springframework.test.context.jdbc.Sql;
import roomescape.member.domain.Member;
import roomescape.reservation.domain.Reservation;
import roomescape.theme.domain.Theme;
import roomescape.time.domain.ReservationTime;

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
    void findReservationsTest() {
        jdbcTemplate.update("INSERT INTO member (name, email) VALUES (?, ?)", "브라운", "brown@abc.com");
        jdbcTemplate.update("INSERT INTO reservation_time(start_at) VALUES (?)", "19:00:00");
        jdbcTemplate.update(
                "INSERT INTO theme (name, description, thumbnail) values (?, ?, ?)",
                "레벨2 탈출", "레벨2 탈출하기", "https://img.jpg");
        jdbcTemplate.update(
                "INSERT INTO reservation (member_id, date, time_id, theme_id) values (?, ?, ?, ?)",
                1, "2024-08-15", 1, 1);
        jdbcTemplate.update(
                "INSERT INTO reservation (member_id, date, time_id, theme_id) values (?, ?, ?, ?)",
                1, "2024-08-20", 1, 1);
        List<Reservation> expected = List.of(
                new Reservation(
                        1L, new Member(1L, "브라운", "brown@abc.com"),
                        LocalDate.of(2024, 8, 15),
                        new ReservationTime(1L, LocalTime.of(19, 0)),
                        new Theme(1L, "레벨2 탈출", "레벨2 탈출하기", "https://img.jpg")),
                new Reservation(
                        2L, new Member(1L, "브라운", "brown@abc.com"),
                        LocalDate.of(2024, 8, 20),
                        new ReservationTime(1L, LocalTime.of(19, 0)),
                        new Theme(1L, "레벨2 탈출", "레벨2 탈출하기", "https://img.jpg")));

        List<Reservation> actual = reservationDao.findReservations();

        assertThat(actual).containsAll(expected);
    }

    @DisplayName("예약을 생성할 수 있다.")
    @Test
    void createReservationTest() {
        jdbcTemplate.update("INSERT INTO member (name, email) VALUES (?, ?)", "브라운", "brown@abc.com");
        jdbcTemplate.update("INSERT INTO reservation_time(start_at) VALUES (?)", "19:00:00");
        jdbcTemplate.update(
                "INSERT INTO theme (name, description, thumbnail) values (?, ?, ?)",
                "레벨2 탈출", "레벨2 탈출하기", "https://img.jpg");
        Reservation reservation = new Reservation(
                new Member(1L, "브라운", "brown@abc.com"),
                LocalDate.of(2024, 8, 15),
                new ReservationTime(1L, LocalTime.of(19, 0)),
                new Theme(1L, "레벨2 탈출", "레벨2 탈출하기", "https://img.jpg"));
        Reservation expected = new Reservation(
                1L, new Member(1L, "브라운", "brown@abc.com"),
                LocalDate.of(2024, 8, 15),
                new ReservationTime(1L, LocalTime.of(19, 0)),
                new Theme(1L, "레벨2 탈출", "레벨2 탈출하기", "https://img.jpg"));

        Reservation actual = reservationDao.createReservation(reservation);

        assertAll(
                () -> assertThat(actual).isEqualTo(expected),
                () -> assertThat(countSavedReservation()).isEqualTo(1)
        );
    }

    @DisplayName("날짜, 시간, 테마가 모두 겹치는 예약이 있다면, 예약을 생성할 수 없다.")
    @Test
    void createReservationTest_whenReservationIsDuplicated() {
        jdbcTemplate.update("INSERT INTO member (name, email) VALUES (?, ?)", "브라운", "brown@abc.com");
        jdbcTemplate.update("INSERT INTO reservation_time(start_at) VALUES (?)", "19:00:00");
        jdbcTemplate.update(
                "INSERT INTO theme (name, description, thumbnail) values (?, ?, ?)",
                "레벨2 탈출", "레벨2 탈출하기", "https://img.jpg");
        jdbcTemplate.update(
                "INSERT INTO reservation (member_id, date, time_id, theme_id) values (?, ?, ?, ?)",
                1, "2024-08-15", 1, 1);
        Reservation reservation = new Reservation(
                new Member(2L, "브리", "bri@abc.com"),
                LocalDate.of(2024, 8, 15),
                new ReservationTime(1L, LocalTime.of(19, 0)),
                new Theme(1L, "레벨2 탈출", "레벨2 탈출하기", "https://img.jpg"));

        assertThatThrownBy(() -> reservationDao.createReservation(reservation))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("해당 날짜, 시간, 테마에 이미 예약이 존재합니다.");
    }

    @DisplayName("예약을 삭제할 수 있다.")
    @Test
    void deleteReservationTest() {
        jdbcTemplate.update("INSERT INTO member (name, email) VALUES (?, ?)", "브라운", "brown@abc.com");
        jdbcTemplate.update("INSERT INTO reservation_time(start_at) VALUES (?)", "19:00:00");
        jdbcTemplate.update(
                "INSERT INTO theme (name, description, thumbnail) values (?, ?, ?)",
                "레벨2 탈출", "레벨2 탈출하기", "https://img.jpg");
        jdbcTemplate.update(
                "INSERT INTO reservation (member_id, date, time_id, theme_id) values (?, ?, ?, ?)",
                1, "2024-08-15", 1, 1);

        reservationDao.deleteReservation(1L);

        assertThat(countSavedReservation()).isZero();
    }

    private int countSavedReservation() {
        return jdbcTemplate.queryForObject("SELECT COUNT(1) FROM reservation", Integer.class);
    }
}
