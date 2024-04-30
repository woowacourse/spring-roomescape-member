package roomescape.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import roomescape.domain.ReservationTime;

import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest
class H2ReservationTimeRepositoryTest {
    @Autowired
    private H2ReservationTimeRepository reservationTimeRepository;

    @DisplayName("예약 시간을 저장한다")
    @Test
    void save() {
        ReservationTime savedReservation = reservationTimeRepository.save(new ReservationTime(
                LocalTime.of(12, 0)));

        assertThat(savedReservation.getStartAt())
                .isEqualTo(LocalTime.of(12, 0));
    }

    @DisplayName("저장된 예약 시간을 조회한다")
    @Test
    void findAll() {
        reservationTimeRepository.save(new ReservationTime(LocalTime.of(12, 0)));
        reservationTimeRepository.save(new ReservationTime(LocalTime.of(13, 0)));

        List<ReservationTime> reservationTimes = reservationTimeRepository.findAll();

        assertThat(reservationTimes)
                .hasSize(2);
    }

    @DisplayName("저장된 예약 시간을 삭제한다")
    @Test
    void deleteById() {
        ReservationTime savedReservation = reservationTimeRepository.save(new ReservationTime(
                LocalTime.of(12, 0)));

        reservationTimeRepository.deleteById(savedReservation.getId());

        assertThat(reservationTimeRepository.findAll())
                .hasSize(0);
    }
}
