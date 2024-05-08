package roomescape.domain.time.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import roomescape.domain.time.ReservationTime;

@JdbcTest
class JdbcReservationTimeRepositoryTest {
    private final ReservationTimeRepository reservationTimeRepository;

    @Autowired
    JdbcReservationTimeRepositoryTest(JdbcTemplate jdbcTemplate) {
        this.reservationTimeRepository = new JdbcReservationTimeRepository(jdbcTemplate);
    }

    @Test
    void 예약_시간을_저장한다() {
        LocalTime startAt = LocalTime.parse("13:00");
        ReservationTime reservationTime = new ReservationTime(startAt);

        reservationTime = reservationTimeRepository.save(reservationTime);

        ReservationTime savedReservationTime = reservationTimeRepository.findById(reservationTime.getId()).get();
        assertThat(savedReservationTime.getStartAt()).isEqualTo(startAt);
    }

    @Test
    void 예약_시간이_이미_존재하면_true를_반환한다() {
        LocalTime startAt = LocalTime.parse("13:00");
        ReservationTime reservationTime = new ReservationTime(startAt);
        reservationTimeRepository.save(reservationTime);

        boolean exists = reservationTimeRepository.existsByStartAt(startAt);

        assertThat(exists).isTrue();
    }

    @Test
    void 예약_시간이_존재하지_않으면_false를_반환한다() {
        LocalTime startAt = LocalTime.parse("13:00");
        ReservationTime reservationTime = new ReservationTime(startAt);
        reservationTimeRepository.save(reservationTime);

        boolean exists = reservationTimeRepository.existsByStartAt(LocalTime.parse("12:00"));

        assertThat(exists).isFalse();
    }

    @Test
    void 모든_예약_시간을_조회한다() {
        LocalTime startAt1 = LocalTime.parse("13:00");
        LocalTime startAt2 = LocalTime.parse("14:00");

        ReservationTime reservationTime1 = new ReservationTime(startAt1);
        ReservationTime reservationTime2 = new ReservationTime(startAt2);

        reservationTimeRepository.save(reservationTime1);
        reservationTimeRepository.save(reservationTime2);

        List<ReservationTime> reservationTimes = reservationTimeRepository.findAll();
        assertAll(
                () -> assertThat(reservationTimes).hasSize(2),
                () -> assertThat(reservationTimes.get(0).getStartAt()).isEqualTo(startAt1),
                () -> assertThat(reservationTimes.get(1).getStartAt()).isEqualTo(startAt2)
        );
    }

    @Test
    void 예약_시간을_삭제한다() {
        LocalTime startAt = LocalTime.parse("13:00");

        ReservationTime reservationTime = new ReservationTime(startAt);
        reservationTime = reservationTimeRepository.save(reservationTime);

        reservationTimeRepository.deleteById(reservationTime.getId());

        assertThat(reservationTimeRepository.findAll()).isEmpty();
    }
}
