package roomescape.reservationtime.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static roomescape.config.TestFixture.futureReservationDate;
import static roomescape.config.TestFixture.reservation;
import static roomescape.config.TestFixture.reservationTime;
import static roomescape.config.TestFixture.theme;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import roomescape.common.exception.DomainType;
import roomescape.common.exception.DuplicatedException;
import roomescape.common.exception.InUseException;
import roomescape.common.exception.NotFoundException;
import roomescape.config.FixedClockTestConfig;
import roomescape.reservation.entity.Reservation;
import roomescape.reservation.repository.JdbcReservationRepository;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.reservationtime.entity.ReservationTime;
import roomescape.theme.entity.Theme;
import roomescape.theme.repository.JdbcThemeRepository;
import roomescape.theme.repository.ThemeRepository;

@JdbcTest
@Import({
        JdbcReservationTimeRepository.class,
        JdbcReservationRepository.class,
        JdbcThemeRepository.class,
        FixedClockTestConfig.class
})
class JdbcReservationTimeRepositoryTest {

    private static final String RESERVATION_NAME = "밀란";
    private static final String THEME_NAME = "테마";
    private static final LocalTime DEFAULT_START_AT = LocalTime.of(11, 0);
    private static final LocalTime SECOND_START_AT = LocalTime.of(14, 0);
    private static final LocalTime FIRST_AVAILABLE_START_AT = LocalTime.of(10, 0);
    private static final LocalTime SECOND_AVAILABLE_START_AT = LocalTime.of(13, 0);
    private static final LocalTime THIRD_AVAILABLE_START_AT = LocalTime.of(16, 0);

    @Autowired
    private Clock clock;

    @Autowired
    private ReservationTimeRepository reservationTimeRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private ThemeRepository themeRepository;

    @Test
    void 예약_시간을_저장하는_테스트() {
        // when
        ReservationTime reservationTime = reservationTimeRepository.save(reservationTime(DEFAULT_START_AT));

        // then
        assertThat(reservationTime.getId()).isPositive();
        assertThat(reservationTime.getStartAt()).isEqualTo(DEFAULT_START_AT);
    }

    @Test
    void 예약_시간을_조회하는_테스트() {
        // given
        ReservationTime reservationTime = reservationTimeRepository.save(reservationTime(DEFAULT_START_AT));

        // when
        ReservationTime foundReservationTime = reservationTimeRepository.findById(reservationTime.getId())
                .orElseThrow(() -> new NotFoundException(DomainType.RESERVATION_TIME, reservationTime.getId()));

        // then
        assertThat(foundReservationTime.getId()).isEqualTo(reservationTime.getId());
        assertThat(foundReservationTime.getStartAt()).isEqualTo(DEFAULT_START_AT);
    }

    @Test
    void 모든_예약_시간을_조회하는_테스트() {
        // given
        ReservationTime reservationTime1 = reservationTimeRepository.save(
                reservationTime(DEFAULT_START_AT));
        ReservationTime reservationTime2 = reservationTimeRepository.save(
                reservationTime(SECOND_START_AT));

        // when
        List<ReservationTime> reservationTimes = reservationTimeRepository.findAll();

        // then
        assertThat(reservationTimes).contains(reservationTime1, reservationTime2);
    }

    @Test
    void 이미_등록된_예약_시간을_저장하면_예외가_발생한다() {
        // given
        reservationTimeRepository.save(reservationTime(DEFAULT_START_AT));

        // when & then
        assertThatThrownBy(() -> reservationTimeRepository.save(reservationTime(DEFAULT_START_AT)))
                .isInstanceOf(DuplicatedException.class)
                .hasMessageContaining(DuplicatedException.clientMessage(DomainType.RESERVATION_TIME));
    }

    @Test
    void 예약_시간을_삭제하는_테스트() {
        // given
        ReservationTime reservationTime = reservationTimeRepository.save(reservationTime(THIRD_AVAILABLE_START_AT));

        // when
        reservationTimeRepository.deleteById(reservationTime.getId());

        // then
        List<ReservationTime> reservationTimes = reservationTimeRepository.findAll();

        assertThat(reservationTimes)
                .extracting(ReservationTime::getId)
                .doesNotContain(reservationTime.getId());
    }

    @Test
    void 특정_날짜와_테마에_예약_가능한_시간을_조회하는_테스트() {
        // given
        ReservationTime reservationTime1 = reservationTimeRepository.save(
                reservationTime(FIRST_AVAILABLE_START_AT));
        ReservationTime reservationTime2 = reservationTimeRepository.save(
                reservationTime(SECOND_AVAILABLE_START_AT));
        ReservationTime reservationTime3 = reservationTimeRepository.save(
                reservationTime(THIRD_AVAILABLE_START_AT));

        Theme theme = themeRepository.save(theme(THEME_NAME));

        LocalDate reservationDate = futureReservationDate(clock);
        Reservation reservation = reservation(RESERVATION_NAME, reservationDate, reservationTime1, theme);
        reservationRepository.save(reservation);

        // when
        List<ReservationTime> availableTimes =
                reservationTimeRepository.findAvailableTimesByDateAndThemeId(reservationDate, theme.getId());

        // then
        assertThat(availableTimes)
                .extracting(ReservationTime::getId)
                .containsExactly(reservationTime2.getId(), reservationTime3.getId());
    }

    @Test
    void 예약이_참조하는_예약시간은_삭제할_수_없다() {
        // given
        ReservationTime reservationTime = reservationTimeRepository.save(reservationTime(FIRST_AVAILABLE_START_AT));
        Theme theme = themeRepository.save(theme(THEME_NAME));
        Reservation reservation = reservation(RESERVATION_NAME, futureReservationDate(clock), reservationTime, theme);
        reservationRepository.save(reservation);

        // when & then
        assertThatThrownBy(() -> reservationTimeRepository.deleteById(reservationTime.getId()))
                .isInstanceOf(InUseException.class);
    }

}
