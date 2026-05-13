package roomescape.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import roomescape.domain.Reservation;
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
        Reservation first = ReservationFixture.createDefaultReservationWithNameAndDate("이프", date, theme,
                reservationTime);
        Reservation second = ReservationFixture.createDefaultReservationWithNameAndDate("아루", date, theme,
                reservationTime);
        reservationRepository.save(first);

        // when & then
        assertThatThrownBy(() -> reservationRepository.save(second))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void 특정_테마의_특정_날짜와_시간에_예약이_존재하는지_확인한다() {
        // given
        LocalDate date = LocalDate.now().plusDays(1);
        Reservation reservation = ReservationFixture.createDefaultReservationWithNameAndDate("이프", date, theme,
                reservationTime);
        reservationRepository.save(reservation);

        // when & then
        Long otherTimeId = 99L;
        LocalDate otherDate = date.plusDays(1);
        boolean exists = reservationRepository.existByDateAndTimeIdAndThemeId(date, reservationTime.getId(),
                theme.getId());
        boolean existsWithOtherTime = reservationRepository.existByDateAndTimeIdAndThemeId(date, otherTimeId,
                theme.getId());
        boolean existsWithOtherDate = reservationRepository.existByDateAndTimeIdAndThemeId(otherDate,
                reservationTime.getId(), theme.getId());

        assertThat(exists).isTrue();
        assertThat(existsWithOtherTime).isFalse();
        assertThat(existsWithOtherDate).isFalse();
    }

    @Test
    void 예약을_삭제한다() {
        // given
        Reservation saved = reservationRepository.save(
                ReservationFixture.createDefaultReservationWithName("이프", theme, reservationTime));

        // when
        reservationRepository.deleteById(saved.getId());

        // then
        assertThat(dataSource.hasReservationById(saved.getId())).isFalse();
    }

    @Test
    void 삭제할_예약이_존재하지_않으면_예외가_발생한다() {
        // given
        Long nonexistentId = 999L;

        // when & then
        assertThatThrownBy(() -> reservationRepository.deleteById(nonexistentId))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("존재하지 않는 예약 정보입니다.");
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
        assertThat(reservations)
                .hasSize(1)
                .extracting(Reservation::getName)
                .containsExactly("두번째");
    }

    @Test
    void 특정_테마와_날짜에_예약된_시간_식별자를_조회한다() {
        // given
        LocalDate date = LocalDate.now().plusDays(1);
        Reservation reservation = ReservationFixture.createDefaultReservationWithNameAndDate("이프", date, theme,
                reservationTime);
        reservationRepository.save(reservation);

        // when
        Set<Long> reservedTimeIds = reservationRepository.findReservedTimeIdsByThemeIdAndDate(theme.getId(), date);

        // then
        assertThat(reservedTimeIds).containsExactly(reservationTime.getId());
    }

    @Test
    void 예약자명으로_예약_목록을_조회한다() {
        // given
        LocalDate firstDate = LocalDate.now().plusDays(1);
        LocalDate secondDate = firstDate.plusDays(1);
        Reservation first = ReservationFixture.createDefaultReservationWithNameAndDate("바니", firstDate, theme,
                reservationTime);
        Reservation second = ReservationFixture.createDefaultReservationWithNameAndDate("웨지", secondDate, theme,
                reservationTime);
        reservationRepository.save(first);
        reservationRepository.save(second);

        // when
        List<Reservation> reservations = reservationRepository.findAllByUserName("바니");

        // then
        assertThat(reservations)
                .hasSize(1)
                .extracting(Reservation::getName)
                .containsExactly("바니");
    }
}
