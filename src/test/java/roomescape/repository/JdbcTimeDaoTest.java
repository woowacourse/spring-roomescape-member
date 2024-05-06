package roomescape.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.domain.ReservationTime;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class JdbcTimeDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private TimeDao timeDao;
    private final RowMapper<ReservationTime> timeRowMapper = (resultSet, rowNum) -> new ReservationTime(
            resultSet.getLong("id"),
            LocalTime.parse(resultSet.getString("start_at"))
    );

    @DisplayName("모든 시간을 조회한다.")
    @Test
    void findAll() {
        // given
        jdbcTemplate.update("INSERT INTO reservation_time (start_at) VALUES (?)", "10:00");

        // when
        List<ReservationTime> allTimes = timeDao.findAll();

        // then
        assertAll(
                () -> assertThat(allTimes.get(0).getId()).isEqualTo(1),
                () -> assertThat(allTimes.get(0).getStartAt()).isEqualTo(LocalTime.of(10, 0))
        );
    }

    @DisplayName("아이디에 해당하는 시간을 조회한다.")
    @Test
    void findById() {
        // given
        jdbcTemplate.update("INSERT INTO reservation_time (start_at) VALUES (?)", "10:00");

        // when
        ReservationTime time = timeDao.findById(1);

        // then
        assertAll(
                () -> assertThat(time.getId()).isEqualTo(1),
                () -> assertThat(time.getStartAt()).isEqualTo(LocalTime.of(10, 0))
        );
    }

    @DisplayName("시간을 저장한다.")
    @Test
    void save() {
        // given && when
        long id = timeDao.save(new ReservationTime(null, LocalTime.of(10, 0)));

        List<ReservationTime> reservationTimes = jdbcTemplate.query("SELECT * FROM reservation_time", timeRowMapper);

        // then
        assertAll(
                () -> assertThat(id).isEqualTo(1),
                () -> assertThat(reservationTimes).hasSize(1),
                () -> assertThat(reservationTimes.get(0).getId()).isEqualTo(1),
                () -> assertThat(reservationTimes.get(0).getStartAt()).isEqualTo(LocalTime.of(10, 0)));

    }

    @DisplayName("해당 ID를 가진 시간이 존재하지 않는다면 IllegalArgumentException 예외를 발생시킨다.")
    @Test
    void findTimeById_AbsenceId_Empty() {
        assertThatThrownBy(() -> timeDao.findById(0L)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("이미 저장된 시간이 있는지 시작 시간으로 확인한다.")
    @Test
    void existByTime() {
        // given
        jdbcTemplate.update("INSERT INTO reservation_time (start_at) VALUES (?)", "10:00");

        // when
        boolean existByDateTime = timeDao.existByTime(LocalTime.of(10, 0));

        // then
        assertThat(existByDateTime).isTrue();
    }

    @DisplayName("아이디에 해당하는 시간을 삭제한다.")
    @Test
    void deleteById() {
        // given
        jdbcTemplate.update("INSERT INTO reservation_time (start_at) VALUES (?)", "10:00");

        // when
        timeDao.deleteById(1L);

        List<ReservationTime> allTimes = timeDao.findAll();

        // then
        assertThat(allTimes).isEmpty();
    }
}
