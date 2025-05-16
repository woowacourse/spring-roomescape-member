package roomescape.reservation.repository;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalTime;
import java.util.List;
import javax.sql.DataSource;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import roomescape.reservation.domain.ReservationTime;
import roomescape.util.repository.TestDataSourceFactory;

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
        String dropSql = "DROP TABLE IF EXISTS reservation, reservation_time, theme, member";
        jdbcTemplate.execute(dropSql);
    }

    @DisplayName("시간 데이터를 저장한다")
    @Test
    void save_time_test() {
        // given
        LocalTime startAt = LocalTime.of(15, 21);
        ReservationTime time = new ReservationTime(null, startAt);

        // when
        Long savedId = jdbcReservationTimeDao.saveAndReturnId(time);

        // then
        ReservationTime savedTime = jdbcReservationTimeDao.findById(savedId).get();
        assertAll(
                () -> assertThat(savedTime.getId()).isEqualTo(7L),
                () -> assertThat(savedTime.getStartAt()).isEqualTo(startAt)
        );

    }

    @DisplayName("저장된 시간 데이터를 모두 조회한다")
    @Test
    void get_all_test() {
        // when
        List<ReservationTime> times = jdbcReservationTimeDao.findAll();

        // then
        assertAll(
                () -> assertThat(times).hasSize(6),
                () -> assertThat(times).extracting(ReservationTime::getStartAt)
                        .containsExactlyInAnyOrder(
                                LocalTime.of(0, 0),
                                LocalTime.of(10, 10),
                                LocalTime.of(12, 0),
                                LocalTime.of(15, 30),
                                LocalTime.of(18, 0),
                                LocalTime.of(20, 30)
                        )
        );

    }

    @DisplayName("저장된 시간 데이터를 삭제한다")
    @Test
    void delete_time_test() {
        // given
        Long removeId = 6L;

        // when
        jdbcReservationTimeDao.deleteById(removeId);

        // then

        assertAll(
                () -> assertThat(jdbcReservationTimeDao.findAll()).hasSize(5),
                () -> assertThat(jdbcReservationTimeDao.findById(removeId).isEmpty()).isTrue()
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
        assertThat(findTime.getStartAt()).isEqualTo(LocalTime.of(0, 0));
    }

    @DisplayName("같은 시간이 있는지 확인한다")
    @Test
    void exist_same_start_at_test() {
        // given
        LocalTime time = LocalTime.of(10, 10);

        // when
        Boolean actual = jdbcReservationTimeDao.existSameStartAt(time);

        // then
        assertThat(actual).isTrue();
    }

}
