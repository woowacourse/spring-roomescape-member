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

        ReservationTime savedReservationTime = reservationTimeRepository.save(reservationTime);

        assertThat(savedReservationTime.getStartAt()).isEqualTo(LocalTime.parse(startAt));
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
        ReservationTime reservationTime = ReservationFixture.reservationTime();
        reservationTime = reservationTimeRepository.save(reservationTime);

        int deletedCount = reservationTimeRepository.deleteById(reservationTime.getId());

        assertThat(deletedCount).isEqualTo(1);
    }

    @Test
    void 존재하지_않는_예약_시간을_삭제하면_0을_반환한다() {
        int deletedCount = reservationTimeRepository.deleteById(0L);

        assertThat(deletedCount).isEqualTo(0);
    }
}
