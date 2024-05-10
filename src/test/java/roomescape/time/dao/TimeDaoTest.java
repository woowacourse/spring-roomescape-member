package roomescape.time.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import roomescape.time.domain.ReservationTime;

@JdbcTest
@Sql(scripts = "/truncate.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class TimeDaoTest {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    private TimeDao timeDao;

    @BeforeEach
    void setUp() {
        timeDao = new TimeDao(jdbcTemplate);
    }

    @DisplayName("저장한 예약 시간을 불러올 수 있다.")
    @Test
    void findTimesTest() {
        jdbcTemplate.update("INSERT INTO reservation_time(start_at) VALUES (?)", "19:00:00");
        jdbcTemplate.update("INSERT INTO reservation_time(start_at) VALUES (?)", "10:00:00");
        List<ReservationTime> expected = List.of(
                new ReservationTime(1L, LocalTime.of(19, 0)),
                new ReservationTime(2L, LocalTime.of(10, 0))
        );

        List<ReservationTime> actual = timeDao.findTimes();

        assertThat(actual).containsAll(expected);
    }

    @DisplayName("id를 통해 예약 시간을 조회할 수 있다.")
    @Test
    void findTimeByIdTest() {
        jdbcTemplate.update("INSERT INTO reservation_time(start_at) VALUES (?)", "19:00:00");
        Optional<ReservationTime> expected = Optional.of(new ReservationTime(1L, LocalTime.of(19, 0)));

        Optional<ReservationTime> actual = timeDao.findTimeById(1L);

        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("해당 id의 예약 시간이 없을 경우, 빈 값을 반환한다.")
    @Test
    void findTimeByIdTest_whenTimeNotExist() {
        Optional<ReservationTime> actual = timeDao.findTimeById(1L);

        assertThat(actual).isEmpty();
    }

    @DisplayName("특정 날짜, 테마가 예약된 시간을 조회할 수 있다.")
    @Test
    void findTimesExistsReservationDateAndThemeIdTest() {
        jdbcTemplate.update("INSERT INTO reservation_time(start_at) VALUES (?)", "19:00:00");
        jdbcTemplate.update("INSERT INTO reservation_time(start_at) VALUES (?)", "10:00:00");
        jdbcTemplate.update("INSERT INTO member (name, email) VALUES (?, ?)", "브라운", "brown@abc.com");
        jdbcTemplate.update(
                "INSERT INTO theme (name, description, thumbnail) values (?, ?, ?)",
                "레벨2 탈출", "레벨2 탈출하기", "https://img.jpg");
        jdbcTemplate.update(
                "INSERT INTO reservation (member_id, date, time_id, theme_id) values (?, ?, ?, ?)",
                1, "2024-08-15", 2, 1);
        List<ReservationTime> expected = List.of(new ReservationTime(2L, LocalTime.of(10, 0)));

        List<ReservationTime> actual = timeDao.findTimesExistsReservationDateAndThemeId(LocalDate.of(2024, 8, 15), 1L);

        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("예약 시간을 생성할 수 있다.")
    @Test
    void createTimeTest() {
        ReservationTime time = new ReservationTime(LocalTime.of(19, 0));
        ReservationTime expected = new ReservationTime(1L, LocalTime.of(19, 0));

        ReservationTime actual = timeDao.createTime(time);

        assertAll(
                () -> assertThat(actual).isEqualTo(expected),
                () -> assertThat(countSavedReservationTime()).isEqualTo(1)
        );
    }

    @DisplayName("이미 존재하는 예약 시간이면, 예약 시간을 생성할 수 없다.")
    @Test
    void createTimeTest_whenStartAtIsOverlapped() {
        jdbcTemplate.update("INSERT INTO reservation_time(start_at) VALUES (?)", "19:00:00");
        ReservationTime time = new ReservationTime(LocalTime.of(19, 0));

        assertThatThrownBy(() -> timeDao.createTime(time))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("해당 시간은 이미 존재합니다.");
    }

    @DisplayName("예약 시간을 삭제할 수 있다.")
    @Test
    void deleteTimeTest() {
        jdbcTemplate.update("INSERT INTO reservation_time(start_at) VALUES (?)", "19:00:00");

        timeDao.deleteTime(1L);

        assertThat(countSavedReservationTime()).isZero();
    }

    @DisplayName("해당 시간에 예약이 있다면, 예약 시간을 삭제할 수 없다.")
    @Test
    void deleteTimeTest_whenReservationUsingTimeExist() {
        jdbcTemplate.update("INSERT INTO reservation_time(start_at) VALUES (?)", "19:00:00");
        jdbcTemplate.update("INSERT INTO member (name, email) VALUES (?, ?)", "브라운", "brown@abc.com");
        jdbcTemplate.update(
                "INSERT INTO theme (name, description, thumbnail) values (?, ?, ?)",
                "레벨2 탈출", "레벨2 탈출하기", "https://img.jpg");
        jdbcTemplate.update(
                "INSERT INTO reservation (member_id, date, time_id, theme_id) values (?, ?, ?, ?)",
                1, "2024-08-15", 1, 1);

        assertThatThrownBy(() -> timeDao.deleteTime(1L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("해당 시간대는 이미 예약되어 있어 삭제할 수 없습니다.");
    }

    private int countSavedReservationTime() {
        return jdbcTemplate.queryForObject("SELECT COUNT(1) FROM reservation_time", Integer.class);
    }
}
