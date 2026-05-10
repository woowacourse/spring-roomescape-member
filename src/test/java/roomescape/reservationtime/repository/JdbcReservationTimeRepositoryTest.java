package roomescape.reservationtime.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import roomescape.reservation.entity.Reservation;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.reservationtime.entity.ReservationTime;
import roomescape.reservationtime.exception.ReservationTimeDuplicatedException;
import roomescape.reservationtime.exception.ReservationTimeNotFoundException;
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

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        jdbcTemplate.update("DELETE FROM reservation");
        jdbcTemplate.update("DELETE FROM reservation_time");
        jdbcTemplate.update("DELETE FROM theme");
    }

    @Test
    void 예약_시간을_저장하는_테스트() {
        LocalTime startAt = LocalTime.of(11, 0);
        ReservationTime reservationTime = reservationTimeRepository.save(ReservationTime.create(startAt));

        assertThat(reservationTime.getId()).isPositive();
        assertThat(reservationTime.getStartAt()).isEqualTo(startAt);
    }

    @Test
    void 예약_시간을_조회하는_테스트() {
        LocalTime startAt = LocalTime.of(12, 0);
        ReservationTime reservationTime = reservationTimeRepository.save(ReservationTime.create(startAt));

        ReservationTime foundReservationTime = reservationTimeRepository.findById(reservationTime.getId())
                .orElseThrow(() -> new ReservationTimeNotFoundException(reservationTime.getId()));

        assertThat(foundReservationTime.getId()).isEqualTo(reservationTime.getId());
        assertThat(foundReservationTime.getStartAt()).isEqualTo(startAt);
    }

    @Test
    void 모든_예약_시간을_조회하는_테스트() {
        ReservationTime reservationTime1 = reservationTimeRepository.save(
                ReservationTime.create(LocalTime.of(13, 0)));
        ReservationTime reservationTime2 = reservationTimeRepository.save(
                ReservationTime.create(LocalTime.of(14, 0)));

        List<ReservationTime> reservationTimes = reservationTimeRepository.findAll();

        assertThat(reservationTimes).contains(reservationTime1, reservationTime2);
    }

    @Test
    void 이미_등록된_예약_시간을_저장하면_예외가_발생한다() {
        LocalTime startAt = LocalTime.of(15, 0);

        reservationTimeRepository.save(ReservationTime.create(startAt));

        assertThatThrownBy(() -> reservationTimeRepository.save(ReservationTime.create(startAt)))
                .isInstanceOf(ReservationTimeDuplicatedException.class)
                .hasMessageContaining("이미 등록된 예약 시간입니다.");
    }

    @Test
    void 예약_시간을_삭제하는_테스트() {
        ReservationTime reservationTime = reservationTimeRepository.save(ReservationTime.create(LocalTime.of(16, 0)));
        reservationTimeRepository.deleteById(reservationTime.getId());

        List<ReservationTime> reservationTimes = reservationTimeRepository.findAll();

        assertThat(reservationTimes)
                .extracting(ReservationTime::getId)
                .doesNotContain(reservationTime.getId());
    }

    @Test
    void 예약_시간을_삭제하면_이를_참조하는_예약도_삭제된다() {
        ReservationTime reservationTime = reservationTimeRepository.save(ReservationTime.create(LocalTime.of(17, 0)));
        Theme theme = themeRepository.save(Theme.create("테마", "테마 설명", "https://example.com/theme.png", Theme.RUNTIME));

        Reservation reservation = Reservation.create(
                "밀란",
                LocalDate.of(2026, 5, 6),
                reservationTime,
                theme
        );

        Reservation savedReservation = reservationRepository.save(reservation);

        reservationTimeRepository.deleteById(reservationTime.getId());

        Optional<Reservation> foundReservation = reservationRepository.findById(savedReservation.getId());
        assertThat(foundReservation).isEmpty();
    }

}
