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
import roomescape.fixture.ReservationFixture;

@JdbcTest
class JdbcReservationTimeRepositoryTest {
    private final ReservationTimeRepository reservationTimeRepository;

    @Autowired
    JdbcReservationTimeRepositoryTest(JdbcTemplate jdbcTemplate) {
        this.reservationTimeRepository = new JdbcReservationTimeRepository(jdbcTemplate);
    }

    @Test
    void 예약_시간을_저장한다() {
        String startAt = "13:00";
        ReservationTime reservationTime = ReservationFixture.reservationTime(startAt);

        reservationTime = reservationTimeRepository.save(reservationTime);

        ReservationTime savedReservationTime = reservationTimeRepository.findById(reservationTime.getId()).get();
        assertThat(savedReservationTime.getStartAt()).isEqualTo(LocalTime.parse(startAt));
    }

    @Test
    void 예약_시간이_이미_존재하면_true를_반환한다() {
        String startAt = "13:00";
        ReservationTime reservationTime = ReservationFixture.reservationTime(startAt);
        reservationTimeRepository.save(reservationTime);

        boolean exists = reservationTimeRepository.existsByStartAt(LocalTime.parse(startAt));

        assertThat(exists).isTrue();
    }

    @Test
    void 예약_시간이_존재하지_않으면_false를_반환한다() {
        String startAt = "13:00";
        ReservationTime reservationTime = ReservationFixture.reservationTime(startAt);
        reservationTimeRepository.save(reservationTime);

        boolean exists = reservationTimeRepository.existsByStartAt(LocalTime.parse("21:00"));

        assertThat(exists).isFalse();
    }

    @Test
    void 모든_예약_시간을_조회한다() {
        ReservationTime reservationTime1 = ReservationFixture.reservationTime("10:00");
        ReservationTime reservationTime2 = ReservationFixture.reservationTime("15:00");
        reservationTimeRepository.save(reservationTime1);
        reservationTimeRepository.save(reservationTime2);

        List<ReservationTime> reservationTimes = reservationTimeRepository.findAll();
        assertAll(
                () -> assertThat(reservationTimes).hasSize(2),
                () -> assertThat(reservationTimes.get(0).getStartAt()).isEqualTo(LocalTime.parse("10:00")),
                () -> assertThat(reservationTimes.get(1).getStartAt()).isEqualTo(LocalTime.parse("15:00"))
        );
    }

    @Test
    void 예약_시간을_삭제한다() {
        ReservationTime reservationTime = ReservationFixture.reservationTime("10:00");
        reservationTime = reservationTimeRepository.save(reservationTime);

        reservationTimeRepository.deleteById(reservationTime.getId());

        assertThat(reservationTimeRepository.findAll()).isEmpty();
    }
}
