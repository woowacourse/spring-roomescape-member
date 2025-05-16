package roomescape.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalTime;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.domain.ReservationTime;

@JdbcTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ReservationTimeDaoTest {

    ReservationTimeDao reservationTimeDao;

    @Autowired
    JdbcTemplate jdbcTemplate;

    ReservationTime savedReservationTime;

    @BeforeEach
    void beforeEach() {
        reservationTimeDao = new ReservationTimeDao(jdbcTemplate);
        ReservationTime reservationTime = new ReservationTime(
            LocalTime.of(10, 5)
        );
        savedReservationTime = reservationTimeDao.save(reservationTime);
    }

    @Test
    @DisplayName("id로 ReservationTime 을 조회 할 수 있다")
    void select_with_id() {
        Optional<ReservationTime> reservationTime = reservationTimeDao.findById(
            savedReservationTime.getId());
        assertThat(reservationTime.get()).isEqualTo(savedReservationTime);
    }

    @Test
    @DisplayName("ReservationTime을 저장한다")
    void save() {
        LocalTime startAt = LocalTime.of(10, 5);
        ReservationTime reservationTime = new ReservationTime(
            startAt
        );
        ReservationTime savedReservationTime = reservationTimeDao.save(reservationTime);
        assertThat(savedReservationTime.getStartAt()).isEqualTo(startAt);
    }

    @Test
    @DisplayName("저장된 ReservationTime 전체를 불러온다")
    void select_all() {
        assertThat(reservationTimeDao.findAll()).contains(savedReservationTime);
    }

    @Test
    @DisplayName("id로 해당 entity를 삭제한다")
    void delete_with_id() {
        Assertions.assertDoesNotThrow(
            () -> reservationTimeDao.deleteById(savedReservationTime.getId()));
    }
}
