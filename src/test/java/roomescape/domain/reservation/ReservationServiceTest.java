package roomescape.domain.reservation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import roomescape.domain.reservation.dto.ReservationCreationRequest;
import roomescape.domain.reservation.dto.ReservationCreationResponse;
import roomescape.domain.reservation.dto.ReservationResponse;
import roomescape.domain.reservationdate.ReservationDate;
import roomescape.domain.reservationdate.ReservationDateRepository;
import roomescape.domain.reservationtime.ReservationTime;
import roomescape.domain.reservationtime.ReservationTimeRepository;
import roomescape.domain.theme.Theme;
import roomescape.domain.theme.ThemeRepository;
import roomescape.support.exception.RoomescapeException;

class ReservationServiceTest {

    @Test
    void 존재하는_예약_시간으로_예약을_생성한다() {
        // given
        FakeReservationRepository reservationRepository = new FakeReservationRepository();
        FakeReservationTimeRepository reservationTimeRepository = new FakeReservationTimeRepository();
        FakeReservationDateRepository reservationDateRepository = new FakeReservationDateRepository();
        FakeThemeRepository themeRepository = new FakeThemeRepository();
        ReservationTime reservationTime = ReservationTime.of(1L, LocalTime.of(10, 0));
        ReservationDate reservationDate = ReservationDate.of(2L, LocalDate.of(2026, 5, 4));
        Theme theme = Theme.of(3L, "공포", "무서운 테마", "theme-url");
        reservationTimeRepository.reservationTime = reservationTime;
        reservationDateRepository.reservationDate = reservationDate;
        themeRepository.theme = theme;
        ReservationService reservationService = new ReservationService(
            reservationRepository,
            reservationTimeRepository,
            reservationDateRepository,
            themeRepository
        );
        ReservationCreationRequest request = new ReservationCreationRequest(
            "보예",
            2L,
            1L,
            3L
        );

        // when
        ReservationCreationResponse response = reservationService.createReservation(request);

        // then
        assertSoftly(softly -> {
            assertThat(response.id()).isEqualTo(1L);
            assertThat(response.name()).isEqualTo("보예");
            assertThat(response.date()).isEqualTo(LocalDate.of(2026, 5, 4));
            assertThat(response.time()).isEqualTo(LocalTime.of(10, 0));
            assertThat(response.theme().name()).isEqualTo("공포");
            assertThat(reservationRepository.savedReservation.getTime()).isEqualTo(reservationTime);
            assertThat(reservationRepository.savedReservation.getTheme()).isEqualTo(theme);
        });
    }

    @Test
    void 존재하지_않는_예약_시간으로_예약을_생성하면_예외가_발생한다() {
        // given
        ReservationService reservationService = new ReservationService(
            new FakeReservationRepository(),
            new FakeReservationTimeRepository(),
            new FakeReservationDateRepository(),
            new FakeThemeRepository()
        );
        ReservationCreationRequest request = new ReservationCreationRequest(
            "보예",
            1L,
            1L,
            1L
        );

        // when & then
        assertThatThrownBy(() -> reservationService.createReservation(request))
            .isInstanceOf(RoomescapeException.class)
            .hasMessage("존재하지 않는 예약 시간대 입니다.");
    }

    @Test
    void 존재하지_않는_테마로_예약을_생성하면_예외가_발생한다() {
        // given
        FakeReservationTimeRepository reservationTimeRepository = new FakeReservationTimeRepository();
        FakeReservationDateRepository reservationDateRepository = new FakeReservationDateRepository();
        reservationTimeRepository.reservationTime = ReservationTime.of(1L, LocalTime.of(10, 0));
        reservationDateRepository.reservationDate = ReservationDate.of(2L, LocalDate.of(2026, 5, 4));
        ReservationService reservationService = new ReservationService(
            new FakeReservationRepository(),
            reservationTimeRepository,
            reservationDateRepository,
            new FakeThemeRepository()
        );
        ReservationCreationRequest request = new ReservationCreationRequest(
            "보예",
            2L,
            1L,
            3L
        );

        // when & then
        assertThatThrownBy(() -> reservationService.createReservation(request))
            .isInstanceOf(RoomescapeException.class)
            .hasMessage("존재하지 않는 테마 입니다.");
    }

    @Test
    void 예약_목록을_조회한다() {
        // given
        FakeReservationRepository reservationRepository = new FakeReservationRepository();
        FakeReservationTimeRepository reservationTimeRepository = new FakeReservationTimeRepository();
        reservationRepository.findAllResult = List.of(
            Reservation.of(
                1L,
                "보예",
                ReservationDate.of(3L, LocalDate.of(2026, 5, 4)),
                ReservationTime.of(2L, LocalTime.of(10, 0)),
                Theme.of(4L, "공포", "무서운 테마", "theme-url")
            )
        );
        ReservationService reservationService = new ReservationService(
            reservationRepository,
            reservationTimeRepository,
            new FakeReservationDateRepository(),
            new FakeThemeRepository()
        );

        // when
        List<ReservationResponse> responses = reservationService.getAllReservations();

        // then
        assertSoftly(softly -> {
            assertThat(responses).hasSize(1);
            assertThat(responses.getFirst().id()).isEqualTo(1L);
            assertThat(responses.getFirst().name()).isEqualTo("보예");
            assertThat(responses.getFirst().date()).isEqualTo(LocalDate.of(2026, 5, 4));
            assertThat(responses.getFirst().time().id()).isEqualTo(2L);
            assertThat(responses.getFirst().time().startAt()).isEqualTo(LocalTime.of(10, 0));
            assertThat(responses.getFirst().theme().id()).isEqualTo(4L);
            assertThat(responses.getFirst().theme().name()).isEqualTo("공포");
        });
    }

    private static class FakeReservationRepository implements ReservationRepository {

        private Reservation savedReservation;
        private List<Reservation> findAllResult = List.of();

        @Override
        public Reservation save(Reservation reservation) {
            savedReservation = reservation;
            return Reservation.of(1L, reservation.getName(), reservation.getDate(), reservation.getTime(),
                reservation.getTheme());
        }

        @Override
        public List<Reservation> findAll() {
            return findAllResult;
        }

        @Override
        public int deleteById(Long id) {
            return 0;
        }

        @Override
        public int countByTimeId(Long timeId) {
            return 0;
        }

        @Override
        public int countByReservationDateId(Long dateId) {
            return 0;
        }

        @Override
        public List<Long> findReservedTimes(Long themeId, Long dateId) {
            return List.of();
        }

        @Override
        public int countByThemeId(Long id) {
            return 0;
        }
    }

    private static class FakeReservationTimeRepository implements ReservationTimeRepository {

        private ReservationTime reservationTime;

        @Override
        public Optional<ReservationTime> findById(Long id) {
            return Optional.ofNullable(reservationTime);
        }

        @Override
        public ReservationTime save(ReservationTime reservationTime) {
            return null;
        }

        @Override
        public List<ReservationTime> findAll() {
            return List.of();
        }

        @Override
        public int deleteById(Long id) {
            return 0;
        }
    }

    private static class FakeReservationDateRepository implements ReservationDateRepository {

        private ReservationDate reservationDate;

        @Override
        public Optional<ReservationDate> findById(Long id) {
            return Optional.ofNullable(reservationDate);
        }

        @Override
        public List<ReservationDate> findAll() {
            return List.of();
        }

        @Override
        public ReservationDate save(ReservationDate reservationDate) {
            return reservationDate;
        }

        @Override
        public int deleteById(Long id) {
            return 0;
        }
    }

    private static class FakeThemeRepository implements ThemeRepository {

        private Theme theme;

        @Override
        public Optional<Theme> findById(Long id) {
            return Optional.ofNullable(theme);
        }

        @Override
        public List<Theme> findAll() {
            return List.of();
        }

        @Override
        public Theme save(Theme theme) {
            return theme;
        }

        @Override
        public int deleteById(Long id) {
            return 0;
        }

        @Override
        public List<Theme> findPopularThemes(int rankLimit, LocalDate startDay, LocalDate endDay) {
            return List.of();
        }
    }
}
