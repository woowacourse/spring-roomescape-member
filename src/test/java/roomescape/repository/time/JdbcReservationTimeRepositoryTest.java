package roomescape.repository.time;

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

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;

@Import({JdbcReservationTimeRepository.class, JdbcReservationRepository.class, JdbcThemeRepository.class})
@JdbcTest
class JdbcReservationTimeRepositoryTest {

    private final ReservationTimeRepository timeRepository;
    private final ReservationRepository reservationRepository;
    private final ThemeRepository themeRepository;

    @Autowired
    public JdbcReservationTimeRepositoryTest(ReservationTimeRepository repository, ReservationRepository reservationRepository, ThemeRepository themeRepository) {
        this.timeRepository = repository;
        this.reservationRepository = reservationRepository;
        this.themeRepository = themeRepository;
    }

    @Test
    void 시간_데이터_생성_테스트() {
        // given
        ReservationTime reservationTime = timeRepository.createReservationTime(new ReservationTime(null, "16:20"));
        assertThat(reservationTime).isNotNull();

        // when
        List<ReservationTime> all = timeRepository.findAll();

        // then
        assertThat(all).hasSize(1);
        assertThat(all.get(0).getId()).isEqualTo(reservationTime.getId());
        assertThat(all.get(0).getStartAt()).isEqualTo(reservationTime.getStartAt());
    }

    @Test
    void 시간_데이터_전체_조회_테스트() {
        // given
        ReservationTime time1 = new ReservationTime(null, "20:43");
        ReservationTime time2 = new ReservationTime(null, "10:00");

        timeRepository.createReservationTime(time1);
        timeRepository.createReservationTime(time2);

        // when
        List<ReservationTime> times = timeRepository.findAll();

        //then
        assertThat(times).hasSize(2);
        assertThat(times)
                .extracting(ReservationTime::getStartAt)
                .anySatisfy(getStartAt -> assertThat(getStartAt).isEqualTo(time1.getStartAt()))
                .anySatisfy(getStartAt -> assertThat(getStartAt).isEqualTo(time2.getStartAt()));
    }

    @Test
    void 시간_데이터_삭제_테스트() {
        // given
        ReservationTime reservationTime = timeRepository.createReservationTime(new ReservationTime(null, "20:43"));
        List<ReservationTime> all = timeRepository.findAll();
        assertThat(all).hasSize(1);
        assertThat(all.getFirst().getId()).isEqualTo(reservationTime.getId());

        // when, then
        assertThatNoException().isThrownBy(() -> timeRepository.deleteById(reservationTime.getId()));
        assertThat(timeRepository.findAll()).hasSize(0);
    }

    @Test
    void 아이디로_특정_데이터_조회_테스트() {
        // given
        ReservationTime test = timeRepository.createReservationTime(new ReservationTime(null, "16:20"));
        Long id = test.getId();

        // when
        Optional<ReservationTime> target = timeRepository.findById(id);

        // then
        assertThat(target)
                .isPresent()
                .hasValueSatisfying(reservation -> {
                    assertThat(reservation.getId()).isEqualTo(id);
                    assertThat(reservation.getStartAt()).isEqualTo(test.getStartAt());
                });
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
        List<ReservationTime> availableTimes = timeRepository.findTimesByDateAndThemeId(tomorrow, theme.getId());

        // then
        assertThat(availableTimes).hasSize(1);
        assertThat(availableTimes).extracting(ReservationTime::getStartAt)
                .anySatisfy(startAt -> assertThat(startAt).isEqualTo(availableTime.getStartAt()));
    }
}