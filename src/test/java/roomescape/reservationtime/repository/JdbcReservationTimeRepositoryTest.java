package roomescape.reservationtime.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static roomescape.config.TestFixture.reservation;
import static roomescape.config.TestFixture.reservationTime;
import static roomescape.config.TestFixture.theme;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import roomescape.reservation.entity.Reservation;
import roomescape.reservation.repository.JdbcReservationRepository;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.reservationtime.entity.ReservationTime;
import roomescape.reservationtime.exception.ReservationTimeDuplicatedException;
import roomescape.reservationtime.exception.ReservationTimeInUseException;
import roomescape.reservationtime.exception.ReservationTimeNotFoundException;
import roomescape.theme.entity.Theme;
import roomescape.theme.repository.JdbcThemeRepository;
import roomescape.theme.repository.ThemeRepository;

@JdbcTest
@Import({JdbcReservationTimeRepository.class, JdbcReservationRepository.class, JdbcThemeRepository.class})
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
        ReservationTime reservationTime = reservationTimeRepository.save(reservationTime(startAt));

        assertThat(reservationTime.getId()).isPositive();
        assertThat(reservationTime.getStartAt()).isEqualTo(startAt);
    }

    @Test
    void 예약_시간을_조회하는_테스트() {
        LocalTime startAt = LocalTime.of(11, 0);
        ReservationTime reservationTime = reservationTimeRepository.save(reservationTime(startAt));

        ReservationTime foundReservationTime = reservationTimeRepository.findById(reservationTime.getId())
                .orElseThrow(() -> new ReservationTimeNotFoundException(reservationTime.getId()));

        assertThat(foundReservationTime.getId()).isEqualTo(reservationTime.getId());
        assertThat(foundReservationTime.getStartAt()).isEqualTo(startAt);
    }

    @Test
    void 모든_예약_시간을_조회하는_테스트() {
        ReservationTime reservationTime1 = reservationTimeRepository.save(
                reservationTime(LocalTime.of(11, 0)));
        ReservationTime reservationTime2 = reservationTimeRepository.save(
                reservationTime(LocalTime.of(14, 0)));

        List<ReservationTime> reservationTimes = reservationTimeRepository.findAll();

        assertThat(reservationTimes).contains(reservationTime1, reservationTime2);
    }

    @Test
    void 이미_등록된_예약_시간을_저장하면_예외가_발생한다() {
        LocalTime startAt = LocalTime.of(11, 0);

        reservationTimeRepository.save(reservationTime(startAt));

        assertThatThrownBy(() -> reservationTimeRepository.save(reservationTime(startAt)))
                .isInstanceOf(ReservationTimeDuplicatedException.class)
                .hasMessageContaining("이미 등록된 예약 시간입니다.");
    }

    @Test
    void 예약_시간을_삭제하는_테스트() {
        ReservationTime reservationTime = reservationTimeRepository.save(reservationTime(LocalTime.of(16, 0)));
        reservationTimeRepository.deleteById(reservationTime.getId());

        List<ReservationTime> reservationTimes = reservationTimeRepository.findAll();

        assertThat(reservationTimes)
                .extracting(ReservationTime::getId)
                .doesNotContain(reservationTime.getId());
    }

    @Test
    void 특정_날짜와_테마에_예약_가능한_시간을_조회하는_테스트() {
        LocalDate date = LocalDate.of(2026, 5, 6);

        ReservationTime reservationTime1 = reservationTimeRepository.save(
                reservationTime(LocalTime.of(10, 0)));
        ReservationTime reservationTime2 = reservationTimeRepository.save(
                reservationTime(LocalTime.of(13, 0)));
        ReservationTime reservationTime3 = reservationTimeRepository.save(
                reservationTime(LocalTime.of(16, 0)));

        Theme theme = themeRepository.save(theme("테마"));

        Reservation reservation = reservation("밀란", date, reservationTime1, theme);
        reservationRepository.save(reservation);

        List<ReservationTime> availableTimes =
                reservationTimeRepository.findAvailableTimesByDateAndThemeId(date, theme.getId());

        assertThat(availableTimes)
                .extracting(ReservationTime::getId)
                .containsExactly(reservationTime2.getId(), reservationTime3.getId());
    }

    @Test
    void 예약이_참조하는_예약시간은_삭제할_수_없다() {
        LocalDate date = LocalDate.of(2026, 5, 10);
        ReservationTime reservationTime = reservationTimeRepository.save(reservationTime(LocalTime.of(10, 0)));
        Theme theme = themeRepository.save(theme("테마"));
        Reservation reservation = reservation("밀란", date, reservationTime, theme);
        reservationRepository.save(reservation);

        assertThatThrownBy(() -> reservationTimeRepository.deleteById(reservationTime.getId()))
                .isInstanceOf(ReservationTimeInUseException.class);
    }

}
