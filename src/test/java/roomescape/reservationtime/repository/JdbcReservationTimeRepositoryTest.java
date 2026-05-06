package roomescape.reservationtime.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import roomescape.reservationtime.entity.ReservationTime;
import roomescape.reservationtime.exception.ReservationTimeNotFoundException;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class JdbcReservationTimeRepositoryTest {

    @Autowired
    private ReservationTimeRepository reservationTimeRepository;

    @Test
    void 예약_시간을_저장하는_테스트() {
        LocalTime startAt = LocalTime.of(11, 0);

        ReservationTime reservationTime = reservationTimeRepository.save(startAt);

        assertThat(reservationTime.getId()).isPositive();
        assertThat(reservationTime.getStartAt()).isEqualTo(startAt);
    }

    @Test
    void 예약_시간을_조회하는_테스트() {
        LocalTime startAt = LocalTime.of(11, 0);
        ReservationTime reservationTime = reservationTimeRepository.save(startAt);

        ReservationTime foundReservationTime = reservationTimeRepository.findById(reservationTime.getId())
                .orElseThrow(() -> new ReservationTimeNotFoundException(reservationTime.getId()));

        assertThat(foundReservationTime.getId()).isEqualTo(reservationTime.getId());
        assertThat(foundReservationTime.getStartAt()).isEqualTo(startAt);
    }

    @Test
    void 모든_예약_시간을_조회하는_테스트() {
        ReservationTime reservationTime1 = reservationTimeRepository.save(LocalTime.of(11, 0));
        ReservationTime reservationTime2 = reservationTimeRepository.save(LocalTime.of(12, 0));

        List<ReservationTime> reservationTimes = reservationTimeRepository.findAll();

        assertThat(reservationTimes).contains(reservationTime1, reservationTime2);
    }

    @Test
    void 예약_시간을_삭제하는_테스트() {
        ReservationTime reservationTime = reservationTimeRepository.save(LocalTime.of(11, 0));
        reservationTimeRepository.deleteById(reservationTime.getId());

        List<ReservationTime> reservationTimes = reservationTimeRepository.findAll();
        assertThat(reservationTimes).doesNotContain(reservationTime);
    }

}
