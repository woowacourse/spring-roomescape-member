package roomescape.dao;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestConstructor;
import roomescape.domain.ReservationTime;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@JdbcTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
public class ReservationTimeDaoTest {

    private final JdbcTemplate jdbcTemplate;
    private final ReservationTimeDao reservationTimeDao;

    public ReservationTimeDaoTest(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.reservationTimeDao = new ReservationTimeDao(jdbcTemplate);
    }

    @Test
    void findAllTest() {
        List<ReservationTime> reservationTimes = reservationTimeDao.findAll();

        assertThat(reservationTimes.size()).isEqualTo(1);
    }

    @Test
    void findByIdTest() {
        ReservationTime reservationTime = reservationTimeDao.findById(1L).get();

        assertThat(reservationTime.getId()).isEqualTo(1L);
    }

    @Test
    void findByWrongIdTest() {
        Optional<ReservationTime> reservationTime = reservationTimeDao.findById(9L);

        assertThat(reservationTime).isEqualTo(Optional.empty());
    }

    @Test
    void insertTest() {
        Long index = jdbcTemplate.queryForObject("SELECT count(*) FROM reservation_time", Long.class);
        Long id = reservationTimeDao.insert("01:01");

        assertThat(id).isEqualTo(index + 1);
    }

    @Test
    void wrongInsertTest() {
        assertThatThrownBy(() -> reservationTimeDao.insert(null))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void deleteByIdTest() {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement("INSERT INTO reservation_time(start_at) VALUES ?",
                    new String[]{"id"});
            ps.setString(1, "01:02");
            return ps;
        }, keyHolder);

        Long key = keyHolder.getKey().longValue();
        reservationTimeDao.deleteById(key);

        assertThat(reservationTimeDao.findAll().stream().map(ReservationTime::getId).toList()).doesNotContain(key);
    }
}
