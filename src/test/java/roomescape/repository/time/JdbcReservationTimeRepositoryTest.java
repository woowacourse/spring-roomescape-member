package roomescape.repository.time;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
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
        ReservationTime saved = timeRepository.createReservationTime(new ReservationTime(null, "16:20"));

        // then
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getStartAt()).isEqualTo("16:20");
    }

    @Test
    void 저장된_모든_예약_시간을_조회한다() {
        // given
        ReservationTime saved1 = timeRepository.createReservationTime(new ReservationTime(null, "20:43"));
        ReservationTime saved2 = timeRepository.createReservationTime(new ReservationTime(null, "10:00"));

        // when
        List<ReservationTime> times = timeRepository.findAll();

        // then
        assertThat(times).containsExactlyInAnyOrder(saved1, saved2);
    }

    @Test
    void 예약_시간을_아이디로_삭제한다() {
        // given
        ReservationTime saved = timeRepository.createReservationTime(new ReservationTime(null, "20:43"));

        // when
        timeRepository.deleteById(saved.getId());

        // then
        assertThat(timeRepository.findAll()).isEmpty();
    }

    @Test
    void 예약_시간을_아이디로_조회한다() {
        // given
        ReservationTime saved = timeRepository.createReservationTime(new ReservationTime(null, "16:20"));

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
        ReservationTime bookedTime = timeRepository.createReservationTime(new ReservationTime("10:00"));
        ReservationTime availableTime = timeRepository.createReservationTime(new ReservationTime("12:00"));
        Theme theme = themeRepository.createTheme(new Theme("테스트", "테스트테마입니다.", ThemeImageUrl.defaultImageUrl().value()));

        LocalDate tomorrow = LocalDate.now().plusDays(1);
        reservationRepository.createReservation(new Reservation("브라운", tomorrow, bookedTime, theme));

        // when
        List<ReservationTime> availableTimes = timeRepository.findByDateAndThemeId(tomorrow, theme.getId());

        // then
        assertThat(availableTimes).containsExactly(availableTime);
    }
}