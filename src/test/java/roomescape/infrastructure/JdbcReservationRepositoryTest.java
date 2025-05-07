package roomescape.infrastructure;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.domain.repository.ReservationRepository;
import roomescape.domain.repository.ReservationTimeRepository;
import roomescape.domain.repository.ThemeRepository;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
class JdbcReservationRepositoryTest {
    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private ReservationTimeRepository reservationTimeRepository;

    @Autowired
    private ThemeRepository themeRepository;

    @Test
    void 예약_시간을_추가할_수_있다() {
        // given
        ReservationTime reservationTime = ReservationTime.createWithoutId(LocalTime.of(10, 0));
        ReservationTime savedReservationTime = reservationTimeRepository.create(reservationTime);
        Theme theme = new Theme(1L, "themeName", "des", "th");
        themeRepository.create(theme);
        Reservation reservation = Reservation.createWithoutId(
                "포라",
                LocalDate.now().plusDays(1),
                savedReservationTime,
                theme
        );

        // when
        reservationRepository.create(reservation);
        List<Reservation> reservationDaoAll = reservationRepository.findAll();

        // then
        assertThat(reservationDaoAll.size()).isEqualTo(1);
    }

    @Test
    void 예약_시간을_조회할_수_있다() {
        // given
        ReservationTime reservationTime = ReservationTime.createWithoutId(LocalTime.of(10, 0));
        ReservationTime savedReservationTime = reservationTimeRepository.create(reservationTime);
        Theme theme = new Theme(1L, "themeName", "des", "th");
        Theme savedTheme = themeRepository.create(theme);
        Reservation reservation = Reservation.createWithoutId(
                "포라",
                LocalDate.now().plusDays(1),
                savedReservationTime,
                savedTheme
        );
        reservationRepository.create(reservation);

        // when
        List<Reservation> reservationDaoAll = reservationRepository.findAll();

        // then
        assertThat(reservationDaoAll.getFirst().getName()).isEqualTo("포라");
    }

    @Test
    void 예약_시간을_삭제할_수_있다() {
        // given
        ReservationTime reservationTime = ReservationTime.createWithoutId(LocalTime.of(10, 0));
        ReservationTime savedReservationTime = reservationTimeRepository.create(reservationTime);
        Theme theme = new Theme(1L, "themeName", "des", "th");
        Theme savedTheme = themeRepository.create(theme);
        Reservation reservation = Reservation.createWithoutId(
                "포라",
                LocalDate.now().plusDays(1),
                savedReservationTime,
                savedTheme);
        reservationRepository.create(reservation);
        int beforeSize = reservationRepository.findAll().size();

        // when
        reservationRepository.delete(savedReservationTime.getId());
        int afterSize = reservationRepository.findAll().size();

        // then
        assertThat(beforeSize).isEqualTo(1);
        assertThat(afterSize).isEqualTo(0);
    }

    @Test
    void timeId로_예약을_조회한다() {
        // given
        ReservationTime reservationTime = ReservationTime.createWithoutId(LocalTime.of(10, 0));
        ReservationTime savedReservationTime = reservationTimeRepository.create(reservationTime);
        Theme theme = new Theme(1L, "themeName", "des", "th");
        Theme savedTheme = themeRepository.create(theme);
        Reservation reservation = Reservation.createWithoutId(
                "포라",
                LocalDate.now().plusDays(1),
                savedReservationTime,
                savedTheme);
        reservationRepository.create(reservation);
        // when
        List<Reservation> foundReservation = reservationRepository.findByTimeId(1L);
        // then
        assertThat(foundReservation.getFirst().getName()).isEqualTo("포라");
    }

    @Test
    void id로_예약을_조회한다() {
        // given
        ReservationTime reservationTime = ReservationTime.createWithoutId(LocalTime.of(10, 0));
        ReservationTime savedReservationTime = reservationTimeRepository.create(reservationTime);
        Theme theme = new Theme(1L, "themeName", "des", "th");
        Theme savedTheme = themeRepository.create(theme);
        Reservation reservation = Reservation.createWithoutId(
                "포라",
                LocalDate.now().plusDays(1),
                savedReservationTime,
                savedTheme);
        reservationRepository.create(reservation);
        // when
        Optional<Reservation> foundReservation = reservationRepository.findById(1L);
        // then
        assertThat(foundReservation.isPresent()).isTrue();
        assertThat(foundReservation.get().getName()).isEqualTo("포라");
    }
}
