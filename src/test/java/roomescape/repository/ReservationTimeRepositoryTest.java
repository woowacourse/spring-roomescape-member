package roomescape.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import roomescape.domain.ReservationTime;

@JdbcTest
class ReservationTimeRepositoryTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    private ReservationTimeRepository reservationTimeRepository;

    @BeforeEach
    void setup() {
        this.reservationTimeRepository = new ReservationTimeRepository(jdbcTemplate);
        jdbcTemplate.update("DELETE FROM reservation;");
        jdbcTemplate.update("DELETE FROM reservation_time;");
    }

    @Test
    void 시간_추가_테스트() {
        // given
        ReservationTime time = new ReservationTime(LocalTime.of(8, 0));

        // when
        Long id = reservationTimeRepository.insert(time);

        // then
        List<ReservationTime> times = reservationTimeRepository.findAll();
        ReservationTime savedTime = reservationTimeRepository.findBy(id).get();
        assertAll(
                () -> assertThat(id).isNotNull(),
                () -> assertThat(times).hasSize(1),
                () -> assertThat(savedTime.getStartAt()).isEqualTo(time.getStartAt()));
    }

    @Test
    void 예약_삭제_테스트() {
        // given
        ReservationTime time1 = new ReservationTime(LocalTime.of(8, 0));
        ReservationTime time2 = new ReservationTime(LocalTime.of(21, 0));
        Long id1 = reservationTimeRepository.insert(time1);
        Long id2 = reservationTimeRepository.insert(time2);

        // when
        int deletedCount = reservationTimeRepository.delete(id1);

        // then
        List<ReservationTime> times = reservationTimeRepository.findAll();
        assertAll(
                () -> assertThat(deletedCount).isEqualTo(1),
                () -> assertThat(times).hasSize(1),
                () -> assertThat(reservationTimeRepository.findBy(id1)).isEmpty());
    }
}
