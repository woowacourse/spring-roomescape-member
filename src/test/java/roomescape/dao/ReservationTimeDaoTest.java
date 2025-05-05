package roomescape.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import roomescape.domain.ReservationTime;

import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static roomescape.fixture.TestFixture.DEFAULT_TIME;

@JdbcTest
class ReservationTimeDaoTest {

    @Autowired
    JdbcTemplate jdbcTemplate;
    ReservationTimeDao reservationTimeDao;

    @BeforeEach
    void setUp() {
        reservationTimeDao = new ReservationTimeDao(jdbcTemplate);
    }

    @Test
    void 예약_시간을_저장할_수_있다() {
        // given
        LocalTime startAt = LocalTime.of(10, 5);
        ReservationTime reservationTime = new ReservationTime(startAt);

        // when
        ReservationTime savedReservationTime = reservationTimeDao.save(reservationTime);

        // then
        assertThat(savedReservationTime.getStartAt()).isEqualTo(startAt);
    }

    @Test
    void 특정_예약_시간을_조회할_수_있다() {
        // given
        Long id = 1L;
        reservationTimeDao.save(DEFAULT_TIME);
        ReservationTime reservationTime = reservationTimeDao.findById(id);

        // when & then
        assertThat(reservationTime.getId()).isEqualTo(id);
    }

    @Test
    void 전체_예약_시간을_조회할_수_있다() {
        // given
        int totalCount = reservationTimeDao.findAll().size();
        ReservationTime newReservationTime = new ReservationTime(LocalTime.of(13, 0));

        // when
        ReservationTime savedReservationTime = reservationTimeDao.save(newReservationTime);

        // then
        assertAll(
                () -> assertThat(reservationTimeDao.findAll().size()).isEqualTo(totalCount + 1),
                () -> assertThat(reservationTimeDao.findAll()).contains(savedReservationTime)
        );
    }

    @Test
    void 특정_예약_시간을_삭제할_수_있다() {
        // given
        reservationTimeDao.save(DEFAULT_TIME);
        int totalCount = reservationTimeDao.findAll().size();

        // when
        reservationTimeDao.deleteById(1L);

        // then
        assertThat(reservationTimeDao.findAll().size()).isEqualTo(totalCount - 1);
    }
}
