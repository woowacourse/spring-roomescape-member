package roomescape.reservationtime.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import roomescape.reservation.entity.Reservation;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.reservationtime.entity.ReservationTime;
import roomescape.reservationtime.exception.ReservationTimeNotFoundException;
import roomescape.reservationtime.exception.ReservationTimeResourceInUseException;
import roomescape.theme.entity.Theme;
import roomescape.theme.repository.ThemeRepository;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class JdbcReservationTimeRepositoryTest {

    @Autowired
    private ReservationTimeRepository reservationTimeRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private ThemeRepository themeRepository;

    @Test
    void 예약_시간을_저장하는_테스트() {
        LocalTime startAt = LocalTime.of(11, 0);
        ReservationTime reservationTime = ReservationTime.of(startAt);
        ReservationTime savedReservationTime = reservationTimeRepository.save(reservationTime);

        assertThat(savedReservationTime.getId()).isPositive();
        assertThat(savedReservationTime.getStartAt()).isEqualTo(startAt);
    }

    @Test
    void 예약_시간을_조회하는_테스트() {
        LocalTime startAt = LocalTime.of(11, 10);
        ReservationTime reservationTime = ReservationTime.of(startAt);
        ReservationTime savedReservationTime = reservationTimeRepository.save(reservationTime);

        ReservationTime foundReservationTime = reservationTimeRepository.findById(savedReservationTime.getId())
                .orElseThrow(() -> new ReservationTimeNotFoundException(savedReservationTime.getId()));

        assertThat(foundReservationTime.getId()).isEqualTo(savedReservationTime.getId());
        assertThat(foundReservationTime.getStartAt()).isEqualTo(startAt);
    }

    @Test
    void 모든_예약_시간을_조회하는_테스트() {
        ReservationTime reservationTime1 = ReservationTime.of(LocalTime.of(11, 20));
        ReservationTime savedReservationTime1 = reservationTimeRepository.save(reservationTime1);
        ReservationTime reservationTime2 = ReservationTime.of(LocalTime.of(12, 20));
        ReservationTime savedReservationTime2 = reservationTimeRepository.save(reservationTime2);

        List<ReservationTime> reservationTimes = reservationTimeRepository.findAll();

        assertThat(reservationTimes).contains(savedReservationTime1, savedReservationTime2);
    }

    @Test
    void 예약_시간을_삭제하는_테스트() {
        ReservationTime reservationTime = ReservationTime.of(LocalTime.of(11, 30));
        ReservationTime savedReservationTime = reservationTimeRepository.save(reservationTime);
        reservationTimeRepository.deleteById(savedReservationTime.getId());

        List<ReservationTime> reservationTimes = reservationTimeRepository.findAll();
        assertThat(reservationTimes).doesNotContain(savedReservationTime);
    }

    @Test
    void 예약이_존재하는_예약_시간은_삭제할_수_없다() {
        ReservationTime reservationTime = ReservationTime.of(LocalTime.of(11, 40));
        ReservationTime savedReservationTime = reservationTimeRepository.save(reservationTime);
        Theme theme = Theme.of("테마", "테마 설명", "https://example.com/theme.png", Duration.ofHours(1));
        Theme savedTheme = themeRepository.save(theme);
        Reservation reservation = Reservation.of("밀란", LocalDate.of(2099, 5, 6), savedReservationTime, savedTheme);
        reservationRepository.save(reservation);

        assertThatThrownBy(() -> reservationTimeRepository.deleteById(savedReservationTime.getId()))
                .isInstanceOf(ReservationTimeResourceInUseException.class);
    }

}
