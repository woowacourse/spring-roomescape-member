package roomescape.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationStatus;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.domain.fixture.ReservationFixture;
import roomescape.domain.fixture.ReservationTimeFixture;
import roomescape.domain.fixture.ThemeFixture;
import roomescape.global.exception.EntityNotFoundException;
import roomescape.service.BaseIntegrationTest;

class ReservationRepositoryTest extends BaseIntegrationTest {

    private final ReservationTime reservationTime = ReservationTimeFixture.createDefaultReservationTime();
    private final Theme theme = ThemeFixture.createThemeWithId();
    @Autowired
    private ReservationRepository reservationRepository;
    @Autowired
    private ReservationDataSource dataSource;

    @BeforeEach
    void setUp() {
        dataSource.clearTable();
        dataSource.clearId();

        dataSource.insertTheme(theme.getName(), theme.getDescription(), theme.getThumbnailImageUrl());
        dataSource.insertReservationTime(reservationTime.getStartAt());
    }

    @Test
    void 예약을_저장하고_ID로_조회한다() {
        // given
        Reservation reservation = ReservationFixture.createDefaultReservationWithName("이프", theme, reservationTime);

        // when
        Reservation saved = reservationRepository.save(reservation);

        // then
        assertThat(saved.getId()).isEqualTo(1L);
        assertThat(dataSource.hasReservationById(saved.getId())).isTrue();
    }

    @Test
    void 동일한_날짜와_시간으로_저장하면_DB_제약조건_에러가_발생한다() {
        // given
        LocalDate date = LocalDate.now().plusDays(1);
        Reservation first = Reservation.create("이프", date, theme,
                reservationTime);
        Reservation second = Reservation.create("아루", date, theme,
                reservationTime);
        reservationRepository.save(first);

        // when & then
        assertThatThrownBy(() -> reservationRepository.save(second)).isInstanceOf(
                DataIntegrityViolationException.class);
    }

    @Test
    void 특정_테마의_특정_날짜와_시간에_활성화된_예약이_존재하는지_확인한다() {
        // given
        LocalDate date = LocalDate.now().plusDays(1);
        Reservation reservation = Reservation.create("이프", date, theme, reservationTime);
        reservationRepository.save(reservation);

        // when & then
        Long otherTimeId = 99L;
        LocalDate otherDate = date.plusDays(1);
        boolean exists = reservationRepository.existsReservedReservation(date, reservationTime.getId(), theme.getId());
        boolean existsWithOtherTime = reservationRepository.existsReservedReservation(date, otherTimeId, theme.getId());
        boolean existsWithOtherDate = reservationRepository.existsReservedReservation(otherDate,
                reservationTime.getId(), theme.getId());

        assertThat(exists).isTrue();
        assertThat(existsWithOtherTime).isFalse();
        assertThat(existsWithOtherDate).isFalse();
    }

    @Test
    void 예약_정보를_수정한다() {
        // given
        Reservation reservation = reservationRepository.save(
                ReservationFixture.createDefaultReservationWithName("바니", theme, reservationTime));

        // when
        Reservation canceledReservation = reservation.cancel();
        reservationRepository.update(canceledReservation);

        // then
        Optional<Reservation> found = reservationRepository.findById(canceledReservation.getId());
        assertThat(found).isPresent();
        assertThat(found.get().getStatus()).isEqualTo(ReservationStatus.CANCELED);
    }

    @Test
    void 존재하지_않는_예약_정보를_수정하면_예외가_발생한다() {
        // given
        Reservation reservation = ReservationFixture.createDefaultReservationWithName("바니", theme, reservationTime);

        // when
        Reservation canceledReservation = reservation.cancel();

        // then
        assertThatThrownBy(() -> reservationRepository.update(canceledReservation)).isInstanceOf(
                EntityNotFoundException.class);
    }

    @Test
    void 페이징_조건에_맞는_예약_목록을_조회한다() {
        // given
        dataSource.insertReservedReservation("이프", LocalDate.now().minusDays(1), 1L, 1L);
        dataSource.insertReservedReservation("이프", LocalDate.now(), 1L, 1L);
        dataSource.insertReservedReservation("이프", LocalDate.now().plusDays(1), 1L, 1L);

        // when
        List<Reservation> reservations = reservationRepository.findAllByPaging(0, 10);

        // then
        assertThat(reservations).hasSize(3);
    }

    @Test
    void 예약_목록을_최신_등록순으로_페이징_조회한다() {
        // given
        dataSource.insertReservedReservation("첫번째", LocalDate.now().minusDays(2), 1L, 1L);
        dataSource.insertReservedReservation("두번째", LocalDate.now().minusDays(1), 1L, 1L);
        dataSource.insertReservedReservation("세번째", LocalDate.now(), 1L, 1L);

        // when
        List<Reservation> reservations = reservationRepository.findAllByPaging(1, 1);

        // then
        assertThat(reservations).hasSize(1).extracting(Reservation::getName).containsExactly("두번째");
    }

    @Test
    void 특정_테마와_날짜에_활성화된_예약이_존재하는_시간_식별자를_조회한다() {
        // given
        ReservationTime canceledTime = ReservationTime.restore(2L, LocalTime.of(11, 0), true);
        dataSource.insertReservationTime(canceledTime.getStartAt());

        LocalDate date = LocalDate.now().plusDays(1);

        Reservation reservation = Reservation.create("이프", date, theme,
                reservationTime);
        reservationRepository.save(reservation);

        Reservation secondReservation = reservationRepository.save(
                Reservation.create("바니", date, theme, canceledTime));

        Reservation canceledReservation = secondReservation.cancel();
        reservationRepository.update(canceledReservation);

        // when
        Set<Long> reservedTimeIds = reservationRepository.findUnavailableTimeIdsByThemeIdAndDate(theme.getId(), date);

        // then
        assertThat(reservedTimeIds).containsExactly(reservationTime.getId());
    }

    @Test
    void 예약자명으로_예약_목록을_조회한다() {
        // given
        LocalDate firstDate = LocalDate.now().plusDays(1);
        LocalDate secondDate = firstDate.plusDays(1);
        Reservation first = Reservation.create("바니", firstDate, theme,
                reservationTime);
        Reservation second = Reservation.create("웨지", secondDate, theme,
                reservationTime);
        reservationRepository.save(first);
        reservationRepository.save(second);

        // when
        List<Reservation> reservations = reservationRepository.findAllByUserName("바니");

        // then
        assertThat(reservations).hasSize(1).extracting(Reservation::getName).containsExactly("바니");
    }

    @Test
    void 특정_시간대에_예약이_존재하는지_반환한다() {
        // given
        LocalDate date = LocalDate.now().plusDays(1);
        reservationRepository.save(
                Reservation.create("바니", date, theme, reservationTime));

        // when & then
        assertThat(reservationRepository.existsByTimeId(reservationTime.getId())).isTrue();
    }
}
