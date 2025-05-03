package roomescape.time.repository;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalTime;
import java.util.List;
import javax.sql.DataSource;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import roomescape.time.domain.ReservationTime;
import roomescape.util.TestDataSourceFactory;

class JdbcReservationTimeDaoTest {

    private JdbcReservationTimeDao jdbcReservationTimeDao;
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setup() {
        DataSource dataSource = TestDataSourceFactory.getEmbeddedDataSource();
        jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcReservationTimeDao = new JdbcReservationTimeDao(jdbcTemplate);
    }

    @AfterEach
    void dropTable() {
        String dropSql = "DROP TABLE IF EXISTS reservation, reservation_time, theme";
        jdbcTemplate.execute(dropSql);
    }

    @DisplayName("시간 데이터를 저장한다")
    @Test
    void save_time_test() {
        // given
        LocalTime startAt = LocalTime.of(15, 21);
        ReservationTime time = new ReservationTime(null, startAt);

        // when
        Long id = jdbcReservationTimeDao.saveAndReturnId(time);

        // then
        String savedStartAt = jdbcTemplate.queryForObject("SELECT start_at FROM reservation_time WHERE id = ?",
                String.class, 7L);
        assertAll(
                () -> assertThat(id).isEqualTo(7L),
                () -> assertThat(savedStartAt).isEqualTo("15:21")
        );

    }

    @DisplayName("저장된 시간 데이터를 모두 조회한다")
    @Test
    void get_all_test() {
        // when
        List<ReservationTime> times = jdbcReservationTimeDao.findAll();

        // then
        List<String> startAts = jdbcTemplate.query("SELECT start_at FROM reservation_time",
                (rs, rowNum) -> rs.getString("start_at"));
        assertAll(
                () -> assertThat(times).hasSize(6),
                () -> assertThat(times).extracting(time -> time.getStartAt().toString())
                        .containsExactlyInAnyOrderElementsOf(startAts)
        );

    }

    @DisplayName("저장된 시간 데이터를 삭제한다")
    @Test
    void delete_time_test() {
        // given
        Long id = 6L;

        // when
        jdbcReservationTimeDao.deleteById(id);

        // then
        String countSql = "SELECT COUNT(1) FROM reservation_time";
        Integer count = jdbcTemplate.queryForObject(countSql, Integer.class);

        String existSql = "SELECT EXISTS(SELECT 1 FROM reservation_time WHERE id = ?)";
        Boolean isExist = jdbcTemplate.queryForObject(existSql, Boolean.class, id);
        assertAll(
                () -> assertThat(count).isEqualTo(5),
                () -> assertThat(isExist).isFalse()
        );
    }

    @DisplayName("id 값에 해당하는 Time 객체를 조회한다")
    @Test
    void find_by_id_test() {
        // given
        Long id = 1L;

        // when
        ReservationTime findTime = jdbcReservationTimeDao.findById(id).get();

        // then
        assertThat(findTime.getId()).isEqualTo(1L);
        assertThat(findTime.getStartAt()).isEqualTo(LocalTime.of(10, 0));
    }

    @DisplayName("같은 시간이 있는지 확인한다")
    @Test
    void exist_same_start_at_test() {
        // given
        LocalTime time = LocalTime.of(10, 10);

        // when
        Boolean actual = jdbcReservationTimeDao.existSameStartAt(time);

        // then
        Assertions.assertThat(actual).isTrue();
    }

}
