package roomescape.reservationtime;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest(properties = "spring.sql.init.mode=never")
@Import(ReservationTimeDao.class)
@Sql(scripts = "classpath:schema-test.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
@Sql(scripts = "classpath:reset-test.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class ReservationTimeDaoTest {
    @Autowired
    private ReservationTimeDao reservationTimeDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void 예약_시간을_저장하고_ID로_조회할_수_있다() {
        ReservationTime saved = reservationTimeDao.save(LocalTime.of(10, 0));

        ReservationTime found = reservationTimeDao.findById(saved.id()).orElseThrow();

        assertThat(found.id()).isEqualTo(saved.id());
        assertThat(found.startAt()).isEqualTo(LocalTime.of(10, 0));
    }

    @Test
    void 예약_시간을_전체_조회할_수_있다() {
        reservationTimeDao.save(LocalTime.of(10, 0));
        reservationTimeDao.save(LocalTime.of(11, 0));

        List<ReservationTime> times = reservationTimeDao.findAll();

        assertThat(times).hasSize(2);
        assertThat(times)
                .extracting(ReservationTime::startAt)
                .containsExactly(LocalTime.of(10, 0), LocalTime.of(11, 0));
    }

    @Test
    void ID로_예약_시간을_삭제할_수_있다() {
        ReservationTime saved = reservationTimeDao.save(LocalTime.of(10, 0));

        reservationTimeDao.delete(saved.id());

        assertThat(reservationTimeDao.findAll()).isEmpty();
    }

    @Test
    void 예약_시간_ID가_없으면_빈_값을_반환한다() {
        assertThat(reservationTimeDao.findById(999L)).isEmpty();
    }
}

