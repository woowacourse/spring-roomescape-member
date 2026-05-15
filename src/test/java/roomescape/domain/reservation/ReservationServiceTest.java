package roomescape.domain.reservation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.domain.reservation.dto.ReservationCreationRequest;
import roomescape.domain.reservation.dto.ReservationCreationResponse;
import roomescape.domain.reservation.dto.ReservationResponse;
import roomescape.domain.reservationdate.ReservationDate;
import roomescape.domain.reservationdate.ReservationDateRepository;
import roomescape.domain.reservationdate.ReservationDateService;
import roomescape.domain.reservationtime.ReservationTime;
import roomescape.domain.reservationtime.ReservationTimeRepository;
import roomescape.domain.reservationtime.ReservationTimeService;
import roomescape.domain.theme.Theme;
import roomescape.domain.theme.ThemeRepository;
import roomescape.domain.theme.ThemeService;
import roomescape.support.exception.RoomescapeException;

class ReservationServiceTest {

    private ReservationService reservationService;
    private FakeReservationRepository reservationRepository;
    private FakeReservationDateRepository reservationDateRepository;
    private FakeReservationTimeRepository reservationTimeRepository;
    private FakeThemeRepository themeRepository;

    @BeforeEach
    void setUp() {
        reservationRepository = new FakeReservationRepository();
        reservationDateRepository = new FakeReservationDateRepository();
        reservationTimeRepository = new FakeReservationTimeRepository();
        themeRepository = new FakeThemeRepository();

        ReservationDateService reservationDateService = new ReservationDateService(
            reservationRepository, reservationDateRepository);
        ReservationTimeService reservationTimeService = new ReservationTimeService(
            reservationTimeRepository, reservationRepository);
        ThemeService themeService = new ThemeService(themeRepository, reservationRepository);

        reservationService = new ReservationService(
            reservationRepository,
            reservationDateService,
            reservationTimeService,
            themeService
        );
    }

    @Test
    @DisplayName("예약을 생성한다.")
    void createReservation() {
        // given
        ReservationDate date = reservationDateRepository.save(
            ReservationDate.createWithoutId(LocalDate.now().plusDays(1)));
        ReservationTime time = reservationTimeRepository.save(ReservationTime.createWithoutId(LocalTime.of(10, 0)));
        Theme theme = themeRepository.save(Theme.createWithoutId("테마", "설명", "url"));

        ReservationCreationRequest request = new ReservationCreationRequest("테스터", date.getId(), time.getId(),
            theme.getId());

        // when
        ReservationCreationResponse response = reservationService.createReservation(request);

        // then
        assertThat(response.name()).isEqualTo("테스터");
        assertThat(reservationRepository.findAll()).hasSize(1);
    }

    @Test
    @DisplayName("중복된 예약 생성 시 예외가 발생한다.")
    void createDuplicateReservation() {
        // given
        ReservationDate date = reservationDateRepository.save(
            ReservationDate.createWithoutId(LocalDate.now().plusDays(1)));
        ReservationTime time = reservationTimeRepository.save(ReservationTime.createWithoutId(LocalTime.of(10, 0)));
        Theme theme = themeRepository.save(Theme.createWithoutId("테마", "설명", "url"));

        reservationService.createReservation(
            new ReservationCreationRequest("테스터1", date.getId(), time.getId(), theme.getId()));

        // when & then
        assertThatThrownBy(() -> reservationService.createReservation(
            new ReservationCreationRequest("테스터2", date.getId(), time.getId(), theme.getId())))
            .isOfAnyClassIn(RoomescapeException.class);
    }

    @Test
    @DisplayName("이름으로 예약을 조회한다.")
    void getReservationsByName() {
        // given
        ReservationDate date = reservationDateRepository.save(
            ReservationDate.createWithoutId(LocalDate.now().plusDays(1)));
        ReservationTime time = reservationTimeRepository.save(ReservationTime.createWithoutId(LocalTime.of(10, 0)));
        Theme theme = themeRepository.save(Theme.createWithoutId("테마", "설명", "url"));
        reservationService.createReservation(
            new ReservationCreationRequest("테스터", date.getId(), time.getId(), theme.getId()));

        // when
        List<ReservationResponse> responses = reservationService.getReservationsByName("테스터");

        // then
        assertThat(responses).hasSize(1);
        assertThat(responses.get(0).name()).isEqualTo("테스터");
    }

    private static class FakeReservationRepository implements ReservationRepository {

        private final List<Reservation> reservations = new ArrayList<>();
        private Long idCounter = 1L;

        @Override
        public Reservation save(Reservation reservation) {
            Reservation saved = Reservation.of(idCounter++, reservation.getName(), reservation.getDate(),
                reservation.getTime(), reservation.getTheme());
            reservations.add(saved);
            return saved;
        }

        @Override
        public List<Reservation> findAll() {
            return reservations;
        }

        @Override
        public int deleteById(Long id) {
            boolean removed = reservations.removeIf(r -> r.getId().equals(id));
            return removed ? 1 : 0;
        }

        @Override
        public int countByTimeId(Long timeId) {
            return (int) reservations.stream().filter(r -> r.getTime().getId().equals(timeId)).count();
        }

        @Override
        public int countByReservationDateId(Long dateId) {
            return (int) reservations.stream().filter(r -> r.getDate().getId().equals(dateId)).count();
        }

        @Override
        public List<Long> findReservedTimes(Long themeId, Long dateId) {
            return reservations.stream()
                .filter(r -> r.getTheme().getId().equals(themeId) && r.getDate().getId().equals(dateId))
                .map(r -> r.getTime().getId())
                .toList();
        }

        @Override
        public int countByThemeId(Long id) {
            return (int) reservations.stream().filter(r -> r.getTheme().getId().equals(id)).count();
        }

        @Override
        public List<Reservation> findByName(String name) {
            return reservations.stream().filter(r -> r.getName().equals(name)).toList();
        }

        @Override
        public Optional<Reservation> findById(Long id) {
            return reservations.stream().filter(r -> r.getId().equals(id)).findFirst();
        }

        @Override
        public int updateReservation(Long id, Long dateId, Long timeId) {
            return 0;
        }

        @Override
        public boolean existsByDateIdAndTimeIdAndThemeId(Long dateId, Long timeId, Long themeId) {
            return reservations.stream().anyMatch(
                r -> r.getDate().getId().equals(dateId) && r.getTime().getId().equals(timeId) && r.getTheme().getId()
                    .equals(themeId));
        }
    }

    private static class FakeReservationDateRepository implements ReservationDateRepository {

        private final List<ReservationDate> dates = new ArrayList<>();
        private Long idCounter = 1L;

        @Override
        public Optional<ReservationDate> findById(Long id) {
            return dates.stream().filter(d -> d.getId().equals(id)).findFirst();
        }

        @Override
        public List<ReservationDate> findAll() {
            return dates;
        }

        @Override
        public ReservationDate save(ReservationDate reservationDate) {
            ReservationDate saved = ReservationDate.of(idCounter++, reservationDate.getPlayDay());
            dates.add(saved);
            return saved;
        }

        @Override
        public int deleteById(Long id) {
            dates.removeIf(d -> d.getId().equals(id));
            return 1;
        }

        @Override
        public boolean existsByPlayDay(LocalDate playDay) {
            return dates.stream().anyMatch(d -> d.getPlayDay().equals(playDay));
        }
    }

    private static class FakeReservationTimeRepository implements ReservationTimeRepository {

        private final List<ReservationTime> times = new ArrayList<>();
        private Long idCounter = 1L;

        @Override
        public Optional<ReservationTime> findById(Long id) {
            return times.stream().filter(t -> t.getId().equals(id)).findFirst();
        }

        @Override
        public ReservationTime save(ReservationTime reservationTime) {
            ReservationTime saved = ReservationTime.of(idCounter++, reservationTime.getStartAt());
            times.add(saved);
            return saved;
        }

        @Override
        public List<ReservationTime> findAll() {
            return times;
        }

        @Override
        public int deleteById(Long id) {
            times.removeIf(t -> t.getId().equals(id));
            return 1;
        }

        @Override
        public boolean existsByStartAt(LocalTime startAt) {
            return times.stream().anyMatch(t -> t.getStartAt().equals(startAt));
        }
    }

    private static class FakeThemeRepository implements ThemeRepository {

        private final List<Theme> themes = new ArrayList<>();
        private Long idCounter = 1L;

        @Override
        public Optional<Theme> findById(Long id) {
            return themes.stream().filter(t -> t.getId().equals(id)).findFirst();
        }

        @Override
        public List<Theme> findAll() {
            return themes;
        }

        @Override
        public Theme save(Theme theme) {
            Theme saved = Theme.of(idCounter++, theme.getName(), theme.getContent(), theme.getUrl());
            themes.add(saved);
            return saved;
        }

        @Override
        public int deleteById(Long id) {
            themes.removeIf(t -> t.getId().equals(id));
            return 1;
        }

        @Override
        public List<Theme> findPopularThemes(int rankLimit, LocalDate startDay, LocalDate endDay) {
            return List.of();
        }
    }
}
