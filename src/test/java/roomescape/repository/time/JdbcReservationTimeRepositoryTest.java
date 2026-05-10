package roomescape.repository.time;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.domain.vo.ThemeImageUrl;
import roomescape.repository.reservation.JdbcReservationRepository;
import roomescape.repository.reservation.ReservationRepository;
import roomescape.repository.theme.JdbcThemeRepository;
import roomescape.repository.theme.ThemeRepository;

@Import({JdbcReservationTimeRepository.class, JdbcReservationRepository.class, JdbcThemeRepository.class})
@JdbcTest
class JdbcReservationTimeRepositoryTest {

    private final ReservationTimeRepository timeRepository;
    private final ReservationRepository reservationRepository;
    private final ThemeRepository themeRepository;

    @Autowired
    public JdbcReservationTimeRepositoryTest(
        ReservationTimeRepository repository,
        ReservationRepository reservationRepository,
        ThemeRepository themeRepository
    ) {
        this.timeRepository = repository;
        this.reservationRepository = reservationRepository;
        this.themeRepository = themeRepository;
    }

    @Test
    void 예약_시간을_저장한다() {
        // when
        ReservationTime time = reservationTime(16, 30);
        ReservationTime saved = timeRepository.createReservationTime(time);

        // then
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getStartAt()).isEqualTo(time.getStartAt());
    }

    @Test
    void 저장된_모든_예약_시간을_조회한다() {
        // given
        ReservationTime saved1 = timeRepository.createReservationTime(reservationTime(16, 30));
        ReservationTime saved2 = timeRepository.createReservationTime(reservationTime(17, 30));

        // when
        List<ReservationTime> times = timeRepository.findAll();

        // then
        assertThat(times).containsExactlyInAnyOrder(saved1, saved2);
    }

    @Test
    void 예약_시간을_아이디로_삭제한다() {
        // given
        ReservationTime saved = timeRepository.createReservationTime(reservationTime(16, 30));

        // when
        timeRepository.deleteById(saved.getId());

        // then
        assertThat(timeRepository.findById(saved.getId())).isEmpty();
    }

    @Test
    void 예약_시간을_아이디로_조회한다() {
        // given
        ReservationTime saved = timeRepository.createReservationTime(reservationTime(16, 30));

        // when
        Optional<ReservationTime> target = timeRepository.findById(saved.getId());

        // then
        assertThat(target).isPresent();
        assertThat(target.get().getId()).isEqualTo(saved.getId());
        assertThat(target.get().getStartAt()).isEqualTo(saved.getStartAt());
    }

    @Test
    void 날짜와_테마아이디로_예약가능한_시간을_조회한다() {
        // given
        ReservationTime bookedTime = timeRepository.createReservationTime(reservationTime(16, 30));
        ReservationTime availableTime = timeRepository.createReservationTime(reservationTime(17, 30));

        Theme theme = themeRepository.createTheme(theme());

        LocalDate tomorrow = LocalDate.now().plusDays(1);
        reservationRepository.createReservation(new Reservation("name", tomorrow, bookedTime, theme));

        // when
        List<ReservationTime> availableTimes = timeRepository.findByDateAndThemeId(tomorrow, theme.getId());

        // then
        assertThat(availableTimes).containsExactly(availableTime);
    }

    private ReservationTime reservationTime(int hour, int minute) {
        return new ReservationTime(LocalTime.of(hour, minute));
    }

    private Theme theme() {
        return new Theme("n", "d.", ThemeImageUrl.defaultImageUrl().value());
    }
}
