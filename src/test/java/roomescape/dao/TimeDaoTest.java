package roomescape.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

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

import roomescape.domain.ReservationTime;

@JdbcTest
@Sql(scripts = "/truncate.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
class TimeDaoTest {
    private final JdbcTemplate jdbcTemplate;
    private final TimeDao timeDao;

    @Autowired
    TimeDaoTest(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.timeDao = new TimeDao(jdbcTemplate);
    }

    @BeforeEach
    void setUp() {
        jdbcTemplate.update("INSERT INTO member(name, email, password) VALUES ('켬미', 'aaa@naver.com', '1111')");
        jdbcTemplate.update("INSERT INTO reservation_time (start_at) VALUES ('10:00')");
        jdbcTemplate.update("INSERT INTO reservation_time (start_at) VALUES ('11:00')");
        jdbcTemplate.update("INSERT INTO theme (name, description, thumbnail) VALUES ('테마1', '설명1' ,'https://image.jpg')");
        jdbcTemplate.update("INSERT INTO reservation (date, member_id, time_id, theme_id) VALUES ('2023-08-05', 1, 1, 1)");
    }

    @DisplayName("DB에서 시간 목록을 읽을 수 있다.")
    @Test
    void readTimes() {
        List<ReservationTime> actual = timeDao.readTimes();
        List<ReservationTime> expected = List.of(
                new ReservationTime(1L, LocalTime.of(10, 0)),
                new ReservationTime(2L, LocalTime.of(11, 0))
        );
        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("해당 날짜에 해당 테마인 시간 목록을 읽을 수 있다.")
    @Test
    void readTimesExistsReservation() {
        List<ReservationTime> actual = timeDao.readTimesExistsReservation("2023-08-05", 1L);
        List<ReservationTime> expected = List.of(new ReservationTime(1L, LocalTime.of(10, 0)));
        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("DB에서 특정 시간을 읽을 수 있다.")
    @Test
    void readTimeById() {
        Optional<ReservationTime> actual = timeDao.readTimeById(1L);
        ReservationTime expected = new ReservationTime(1L, LocalTime.of(10, 0));
        assertThat(actual.get()).isEqualTo(expected);
    }

    @DisplayName("DB에서 없는 시간을 조회하려고하면 Optional 값이 넘어온다.")
    @Test
    void readThemeById_throwException() {
        Optional<ReservationTime> actual = timeDao.readTimeById(3L);
        assertThat(actual.isEmpty()).isEqualTo(true);
    }

    @DisplayName("시간 목록에 해당 시작 시간이 있는지 알 수 있다.")
    @ParameterizedTest
    @CsvSource(value = {"10:00, true", "12:00, false"})
    void existsTimeByStartAt(String startAt, boolean expected) {
        boolean actual = timeDao.existsTimeByStartAt(startAt);
        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("DB에 시간을 추가할 수 있다.")
    @Test
    void createTime() {
        ReservationTime time = new ReservationTime(LocalTime.of(12, 0));

        timeDao.createTime(time);
        Integer count = jdbcTemplate.queryForObject("SELECT count(1) from reservation_time", Integer.class);

        assertThat(count).isEqualTo(3);
    }

    @DisplayName("DB에 시간을 삭제할 수 있다.")
    @Test
    void deleteTime() {
        timeDao.deleteTime(2L);
        Integer count = jdbcTemplate.queryForObject("SELECT count(1) from reservation_time", Integer.class);

        assertThat(count).isEqualTo(1);
    }
}
