package roomescape.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import roomescape.entity.ReservationTime;

import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@JdbcTest
@ActiveProfiles("test")
@Sql(scripts = {"/schema.sql", "/test.sql"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class H2ReservationTimeDaoTest {

    /**
     * test.sql에 초기값이 설정되어 있습니다.
     * 시간 데이터가 6개 들어가있고, 테스트 전 data.sql 확인바랍니다.
     */

    private ReservationTimeDao reservationTimeDao;

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        reservationTimeDao = new H2ReservationTimeDao(jdbcTemplate);
    }

    @Test
    void 시간을_저장한다() {
        ReservationTime reservationTime = new ReservationTime(LocalTime.of(22, 0));

        ReservationTime saved = reservationTimeDao.save(reservationTime);

        ReservationTime expected = new ReservationTime(7L, LocalTime.of(22, 0));
        assertThat(saved).isEqualTo(expected);
    }

    @Test
    void 모든_시간을_조회한다() {
        List<ReservationTime> allReservationTimes = reservationTimeDao.findAll();

        assertThat(allReservationTimes).hasSize(6);
    }

    @Test
    void 시간을_삭제한다() {
        reservationTimeDao.save(new ReservationTime(LocalTime.of(22, 0)));

        reservationTimeDao.deleteById(7L);

        List<ReservationTime> allReservationTimes = reservationTimeDao.findAll();
        assertThat(allReservationTimes).hasSize(6);
    }

    @Test
    void id로_시간을_조회한다() {
        ReservationTime expected = new ReservationTime(2L, LocalTime.of(12, 0));

        assertThat(reservationTimeDao.findById(2L).get())
            .isEqualTo(expected);
    }

    @Test
    void 동일한_시간이_존재하는지_확인한다() {
        assertAll(
            () -> assertThat(reservationTimeDao.isExistByTime(LocalTime.of(10, 0))).isTrue(),
            () -> assertThat(reservationTimeDao.isExistByTime(LocalTime.of(23, 0))).isFalse()
        );
    }
}

