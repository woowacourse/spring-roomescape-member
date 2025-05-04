package roomescape.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import roomescape.domain.ReservationTime;

@JdbcTest
class JdbcReservationTimeDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private JdbcReservationTimeDao jdbcReservationTimeDao;

    @BeforeEach
    void setUp() {
        jdbcReservationTimeDao = new JdbcReservationTimeDao(jdbcTemplate);
    }

    @Test
    @DisplayName("예약 시간 목록을 조회한다")
    void returnReservationTimeList() {
        // when
        List<ReservationTime> reservationTimes = jdbcReservationTimeDao.findAll();
        ReservationTime reservationTime = reservationTimes.getFirst();

        // then
        assertAll(
                () -> assertThat(reservationTimes).hasSize(1),
                () -> assertThat(reservationTime)
                        .extracting("id", "startAt")
                        .containsExactly(1L, LocalTime.of(10, 0))
        );
    }

    @Test
    @DisplayName("예약 시간 목록중 하나를 조회한다")
    void returnReservationTimeById() {
        // when
        ReservationTime reservationTime = jdbcReservationTimeDao.findById(1L);

        // then
        assertThat(reservationTime)
                .extracting("id", "startAt")
                .containsExactly(1L, LocalTime.of(10, 0));
    }

    @Test
    @DisplayName("예약 시간을 저장한다")
    void saveReservationTime() {
        // given
        ReservationTime time = new ReservationTime(LocalTime.of(11, 0));

        // when
        Long savedTimeId = jdbcReservationTimeDao.save(time);
        Boolean result = existsReservationTimeById(savedTimeId);

        // then 
        assertThat(savedTimeId).isNotNull();
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("예약 시간을 삭제한다")
    void deleteReservationTime() {
        // given
        Boolean exists = existsReservationTimeById(1L);

        // when
        jdbcReservationTimeDao.deleteById(1L);
        Boolean notExists = existsReservationTimeById(1L);

        // then
        assertThat(exists).isTrue();
        assertThat(notExists).isFalse();
    }

    private Boolean existsReservationTimeById(Long id) {
        return jdbcTemplate.queryForObject(
                "select exists(select 1 from reservation_time where id = ?)",
                Boolean.class,
                id
        );
    }
}
