package roomescape.repository.reservation;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.repository.time.JdbcReservationTimeRepository;
import roomescape.repository.time.ReservationTimeRepository;

import java.util.List;
import roomescape.repository.theme.JdbcThemeRepository;
import roomescape.repository.theme.ThemeRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

@Import({JdbcReservationRepository.class, JdbcReservationTimeRepository.class, JdbcThemeRepository.class})
@JdbcTest
class JdbcReservationRepositoryTest {

    private static final ReservationTime RESERVATION_TIME = new ReservationTime("12:30");
    private static final Theme THEME = new Theme("테마명", "설명", "url");

    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository timeRepository;
    private final ThemeRepository themeRepository;

    @Autowired
    public JdbcReservationRepositoryTest(
        ReservationRepository repository,
        ReservationTimeRepository timeRepository,
        ThemeRepository themeRepository
    ) {
        this.reservationRepository = repository;
        this.timeRepository = timeRepository;
        this.themeRepository = themeRepository;
    }


    @Test
    void 예약을_저장한다() {
        // given
        ReservationTime savedTime = timeRepository.createReservationTime(RESERVATION_TIME);
        Theme savedTheme = themeRepository.createTheme(THEME);

        // when
        Reservation saved = reservationRepository.createReservation(new Reservation("name", "2026-04-30", savedTime, savedTheme));

        // then
        assertThat(saved.getTime()).isEqualTo(savedTime);
        assertThat(saved.getTheme()).isEqualTo(savedTheme);
    }

    @Test
    void 전체_예약_목록을_조회한다() {
        // given
        ReservationTime savedTime1 = timeRepository.createReservationTime(new ReservationTime("13:43"));
        ReservationTime savedTime2 = timeRepository.createReservationTime(new ReservationTime("10:00"));

        Theme savedTheme = themeRepository.createTheme(new Theme("a", "a", "a_url"));

        reservationRepository.createReservation(new Reservation("브라운", "2026-04-28", savedTime1, savedTheme));
        reservationRepository.createReservation(new Reservation("제임스", "2026-04-29", savedTime2, savedTheme));

        // when
        List<Reservation> reservations = reservationRepository.findAll();

        // then
        assertThat(reservations).hasSize(2);

        assertThat(reservations)
            .extracting(Reservation::getTime)
            .anySatisfy(time -> assertThat(time).isEqualTo(savedTime1))
            .anySatisfy(time -> assertThat(time).isEqualTo(savedTime2));

        assertThat(reservations)
            .extracting(Reservation::getTheme)
            .allSatisfy(theme -> assertThat(theme.getNameValue()).isEqualTo("a"));
    }

    @Test
    void 저장되어_있는_예약을_아이디로_조회하여_삭제한다() {
        // given
        ReservationTime time = timeRepository.createReservationTime(new ReservationTime("13:43"));
        Theme theme = themeRepository.createTheme(new Theme("a", "a", "a_url"));

        Reservation saved = reservationRepository.createReservation(new Reservation("브라운", "2026-04-28", time, theme));

        // when & then
        assertThatCode(() -> reservationRepository.deleteById(saved.getId()))
            .doesNotThrowAnyException();
    }

    @Test
    void 저장되어_있는_예약을_아이디로_조회한다() {
        // given
        ReservationTime savedTime = timeRepository.createReservationTime(RESERVATION_TIME);
        Theme savedTheme = themeRepository.createTheme(THEME);

        Reservation saved = reservationRepository.createReservation(new Reservation("브라운", "2026-05-01", savedTime, savedTheme));

        // when
        Reservation target = reservationRepository.findById(saved.getId());

        // then
        assertThat(target.getId()).isEqualTo(saved.getId());
        assertThat(target.getName()).isEqualTo(saved.getName());
        assertThat(target.getDate()).isEqualTo(saved.getDate());
        assertThat(target.getTime()).isEqualTo(saved.getTime());
        assertThat(target.getTheme()).isEqualTo(saved.getTheme());
    }

    @Test
    void 특정_시간_ID를_가지는_예약이_있으면_TRUE를_반환한다() {
        // given
        ReservationTime savedTime = timeRepository.createReservationTime(RESERVATION_TIME);
        Theme savedTheme = themeRepository.createTheme(THEME);

        Reservation saved = reservationRepository.createReservation(new Reservation("브라운", "2026-05-01", savedTime, savedTheme));

        // when
        boolean exists = reservationRepository.existsByTimeId(saved.getTimeId());

        // then
        assertThat(exists).isTrue();
    }

    @Test
    void 특정_시간_ID를_가지는_예약이_없으면_FALSE를_반환한다() {
        // given
        ReservationTime savedTime = timeRepository.createReservationTime(RESERVATION_TIME);
        ReservationTime otherTime = timeRepository.createReservationTime(
            new ReservationTime(2L, "17:00"));
        Theme savedTheme = themeRepository.createTheme(THEME);

        reservationRepository.createReservation(
            new Reservation("브라운", "2026-05-01", savedTime, savedTheme));

        // when
        boolean exists = reservationRepository.existsByTimeId(otherTime.getId());

        // then
        assertThat(exists).isFalse();
    }
}