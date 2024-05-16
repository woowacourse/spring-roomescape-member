package roomescape.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;

import roomescape.domain.Member;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Role;
import roomescape.domain.Theme;

@JdbcTest
@Sql(scripts = "/truncate.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
class ReservationDaoTest {
    private final JdbcTemplate jdbcTemplate;
    private final ReservationDao reservationDao;

    @Autowired
    ReservationDaoTest(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.reservationDao = new ReservationDao(jdbcTemplate);
    }

    @BeforeEach
    void setUp() {
        jdbcTemplate.update("INSERT INTO member(name, email, password) VALUES ('켬미', 'aaa@naver.com', '1111')");
        jdbcTemplate.update("INSERT INTO reservation_time (start_at) VALUES ('10:00')");
        jdbcTemplate.update("INSERT INTO reservation_time (start_at) VALUES ('11:00')");
        jdbcTemplate.update("INSERT INTO theme (name, description, thumbnail) VALUES ('테마1', '설명1' ,'https://image.jpg')");
        jdbcTemplate.update("INSERT INTO reservation (date, member_id, time_id, theme_id) VALUES ('2023-08-05', 1, 1, 1)");
    }

    @DisplayName("DB에서 예약 목록을 읽을 수 있다.")
    @Test
    void readReservations() {
        List<Reservation> actual = reservationDao.readReservations();
        List<Reservation> expected = List.of(new Reservation(
                1L,
                LocalDate.of(2023, 8, 5),
                new Member(1L, "켬미", "aaa@naver.com", "1111", Role.MEMBER),
                new ReservationTime(1L, LocalTime.of(10, 0)),
                new Theme(1L, "테마1", "설명1", "https://image.jpg")
        ));
        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("예약 중에 해당 시간(time id)에 예약이 있는지 알 수 있다.")
    @ParameterizedTest
    @CsvSource(value = {"1, true", "2, false"})
    void existsReservationByTimeId(long timeId, boolean expected) {
        boolean actual = reservationDao.existsReservationByTimeId(timeId);
        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("예약 중에 해당 테마(theme id)인 예약이 있는지 알 수 있다.")
    @ParameterizedTest
    @CsvSource(value = {"1, true", "2, false"})
    void existsReservationByThemeId(long themeId, boolean expected) {
        boolean actual = reservationDao.existsReservationByThemeId(themeId);
        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("예약 중에 해당 시간(time id)에 해당 테마(theme id)인 예약이 있는지 알 수 있다.")
    @ParameterizedTest
    @CsvSource(value = {"1, 1, true", "1, 2, false"})
    void existsReservationByDateAndTimeIdAndThemeId(long timeId, long themeId, boolean expected) {
        LocalDate date = LocalDate.of(2023, 8, 5);
        boolean actual = reservationDao.existsReservationByDateAndTimeIdAndThemeId(date, timeId, themeId);
        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("DB에 예약을 추가할 수 있다.")
    @Test
    void createReservation() {
        Reservation reservation = new Reservation(
                1L,
                LocalDate.of(2023, 8, 5),
                new Member(1L, "켬미", "aaa@naver.com", "1111", Role.MEMBER),
                new ReservationTime(1L, LocalTime.of(10, 0)),
                new Theme(1L, "테마1", "설명1", "https://image.jpg")
        );
        reservationDao.createReservation(reservation);

        Integer count = jdbcTemplate.queryForObject("SELECT count(1) from reservation", Integer.class);
        assertThat(count).isEqualTo(2);
    }

    @DisplayName("DB에 예약을 삭제할 수 있다.")
    @Test
    void deleteReservation() {
        reservationDao.deleteReservation(1L);

        Integer count = jdbcTemplate.queryForObject("SELECT count(1) from reservation", Integer.class);
        assertThat(count).isEqualTo(0);
    }
}
