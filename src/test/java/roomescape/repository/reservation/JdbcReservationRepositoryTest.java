package roomescape.repository.reservation;

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
import roomescape.repository.theme.JdbcThemeRepository;
import roomescape.repository.theme.ThemeRepository;
import roomescape.repository.time.JdbcReservationTimeRepository;
import roomescape.repository.time.ReservationTimeRepository;

@Import({JdbcReservationRepository.class, JdbcReservationTimeRepository.class, JdbcThemeRepository.class})
@JdbcTest
class JdbcReservationRepositoryTest {

    private static final ReservationTime RESERVATION_TIME = new ReservationTime("12:30");
    private static final Theme THEME = new Theme("테마명", "설명", ThemeImageUrl.defaultImageUrl().value());

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
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        Reservation saved = reservationRepository.createReservation(
            new Reservation("name", tomorrow, savedTime, savedTheme));

        // then
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getTime()).isEqualTo(savedTime);
        assertThat(saved.getTheme()).isEqualTo(savedTheme);
    }

    @Test
    void 전체_예약_목록을_조회한다() {
        // given
        ReservationTime savedTime1 = timeRepository.createReservationTime(new ReservationTime("13:43"));
        ReservationTime savedTime2 = timeRepository.createReservationTime(new ReservationTime("10:00"));

        Theme savedTheme = themeRepository.createTheme(THEME);

        LocalDate tomorrow = LocalDate.now().plusDays(1);
        reservationRepository.createReservation(new Reservation("브라운", tomorrow, savedTime1, savedTheme));
        reservationRepository.createReservation(new Reservation("제임스", tomorrow, savedTime2, savedTheme));

        // when
        List<Reservation> reservations = reservationRepository.findAll();

        // then
        assertThat(reservations)
            .extracting(Reservation::getTime)
            .containsExactlyInAnyOrder(savedTime1, savedTime2);

        assertThat(reservations)
            .extracting(Reservation::getTheme)
            .containsOnly(savedTheme);
    }

    @Test
    void 예약을_아이디로_삭제한다() {
        // given
        ReservationTime time = timeRepository.createReservationTime(RESERVATION_TIME);
        Theme theme = themeRepository.createTheme(THEME);

        LocalDate tomorrow = LocalDate.now().plusDays(1);
        Reservation saved = reservationRepository.createReservation(new Reservation("브라운", tomorrow, time, theme));

        // when
        reservationRepository.deleteById(saved.getId());

        // then
        assertThat(reservationRepository.findById(saved.getId())).isEmpty();
    }

    @Test
    void 저장되어_있는_예약을_아이디로_조회한다() {
        // given
        ReservationTime savedTime = timeRepository.createReservationTime(RESERVATION_TIME);
        Theme savedTheme = themeRepository.createTheme(THEME);

        LocalDate tomorrow = LocalDate.now().plusDays(1);
        Reservation saved = reservationRepository.createReservation(
            new Reservation("브라운", tomorrow, savedTime, savedTheme));

        // when
        Optional<Reservation> target = reservationRepository.findById(saved.getId());

        // then
        assertThat(target.get().getId()).isEqualTo(saved.getId());
        assertThat(target.get().getName()).isEqualTo(saved.getName());
        assertThat(target.get().getDateValue()).isEqualTo(saved.getDateValue());
        assertThat(target.get().getTime()).isEqualTo(saved.getTime());
        assertThat(target.get().getTheme()).isEqualTo(saved.getTheme());
    }

    @Test
    void 특정_시간_ID를_가지는_예약이_있으면_TRUE를_반환한다() {
        // given
        ReservationTime savedTime = timeRepository.createReservationTime(RESERVATION_TIME);
        Theme savedTheme = themeRepository.createTheme(THEME);

        LocalDate tomorrow = LocalDate.now().plusDays(1);
        Reservation saved = reservationRepository.createReservation(
            new Reservation("브라운", tomorrow, savedTime, savedTheme));

        // when
        boolean exists = reservationRepository.existsByTimeId(saved.getTimeId());

        // then
        assertThat(exists).isTrue();
    }

    @Test
    void 특정_시간_ID를_가지는_예약이_없으면_FALSE를_반환한다() {
        // given
        ReservationTime savedTime = timeRepository.createReservationTime(RESERVATION_TIME);
        ReservationTime otherTime = timeRepository.createReservationTime(new ReservationTime("17:00"));

        Theme savedTheme = themeRepository.createTheme(THEME);

        LocalDate tomorrow = LocalDate.now().plusDays(1);
        reservationRepository.createReservation(
            new Reservation("브라운", tomorrow, savedTime, savedTheme));

        // when
        boolean exists = reservationRepository.existsByTimeId(otherTime.getId());

        // then
        assertThat(exists).isFalse();
    }
}