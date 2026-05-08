package roomescape.reservation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.domain.reservation.Reservation;
import roomescape.repository.reservation.MemoryReservationRepository;
import roomescape.service.reservation.ReservationService;
import roomescape.domain.reservationtime.ReservationTime;
import roomescape.repository.reservationtime.ReservationTimeRepository;
import roomescape.service.reservationtime.ReservationTimeService;
import roomescape.domain.theme.Theme;
import roomescape.repository.theme.ThemeRepository;
import roomescape.service.theme.ThemeService;

class ReservationServiceTest {

    @Test
    @DisplayName("예약을 저장한다")
    void save() {
        Fixture fixture = new Fixture();
        Theme theme = fixture.themeService.save("미술관의 밤", "추리 테마", "https://example.com/theme.png");
        ReservationTime time = fixture.reservationTimeService.save(LocalTime.parse("10:00"));

        Reservation saved = fixture.reservationService.save("쿠다", LocalDate.parse("2026-08-06"), theme.getId(), time.getId());

        assertThat(saved.getId()).isEqualTo(1L);
        assertThat(saved.getTheme().getId()).isEqualTo(theme.getId());
        assertThat(saved.getTime().getId()).isEqualTo(time.getId());
    }

    @Test
    @DisplayName("같은 날짜, 테마, 시간 조합의 중복 예약은 저장할 수 없다")
    void saveDuplicateReservation() {
        Fixture fixture = new Fixture();
        Theme theme = fixture.themeService.save("미술관의 밤", "추리 테마", "https://example.com/theme.png");
        ReservationTime time = fixture.reservationTimeService.save(LocalTime.parse("10:00"));
        LocalDate date = LocalDate.parse("2026-08-06");
        fixture.reservationService.save("쿠다", date, theme.getId(), time.getId());

        assertThrows(
                IllegalArgumentException.class,
                () -> fixture.reservationService.save("아루", date, theme.getId(), time.getId())
        );
    }

    @Test
    @DisplayName("예약을 삭제한다")
    void deleteById() {
        Fixture fixture = new Fixture();
        Theme theme = fixture.themeService.save("미술관의 밤", "추리 테마", "https://example.com/theme.png");
        ReservationTime time = fixture.reservationTimeService.save(LocalTime.parse("10:00"));
        Reservation saved = fixture.reservationService.save("쿠다", LocalDate.parse("2026-08-06"), theme.getId(), time.getId());

        fixture.reservationService.deleteById(saved.getId());

        assertThat(fixture.reservationRepository.findAll()).isEmpty();
    }

    private static class Fixture {
        private final MemoryReservationRepository reservationRepository = new MemoryReservationRepository();
        private final ReservationTimeService reservationTimeService =
                new ReservationTimeService(new TestReservationTimeRepository(), reservationRepository);
        private final ThemeService themeService =
                new ThemeService(new TestThemeRepository(), reservationRepository);
        private final ReservationService reservationService =
                new ReservationService(reservationRepository, reservationTimeService, themeService);
    }

    private static class TestThemeRepository implements ThemeRepository {

        private final List<Theme> themes = new ArrayList<>();
        private long nextId = 1L;

        @Override
        public List<Theme> findAll() {
            return List.copyOf(themes);
        }

        @Override
        public Optional<Theme> findById(final long id) {
            return themes.stream()
                    .filter(theme -> theme.getId() == id)
                    .findFirst();
        }

        @Override
        public void deleteById(final long id) {
            themes.removeIf(theme -> theme.getId() == id);
        }

        @Override
        public Theme save(final Theme theme) {
            Theme saved = theme.withId(nextId++);
            themes.add(saved);
            return saved;
        }

        @Override
        public boolean existsByName(final String name) {
            return themes.stream().anyMatch(theme -> theme.getName().equals(name));
        }

        @Override
        public List<Theme> findPopularThemes(final int period, final int limit) {
            throw new UnsupportedOperationException();
        }
    }

    private static class TestReservationTimeRepository implements ReservationTimeRepository {

        private final List<ReservationTime> reservationTimes = new ArrayList<>();
        private long nextId = 1L;

        @Override
        public List<ReservationTime> findAll() {
            return List.copyOf(reservationTimes);
        }

        @Override
        public Optional<ReservationTime> findById(final long timeId) {
            return reservationTimes.stream()
                    .filter(reservationTime -> reservationTime.getId() == timeId)
                    .findFirst();
        }

        @Override
        public void deleteById(final long timeId) {
            reservationTimes.removeIf(reservationTime -> reservationTime.getId() == timeId);
        }

        @Override
        public ReservationTime save(final ReservationTime reservationTime) {
            ReservationTime saved = reservationTime.withId(nextId++);
            reservationTimes.add(saved);
            return saved;
        }

        @Override
        public boolean existsByStartAt(final LocalTime startAt) {
            return reservationTimes.stream()
                    .anyMatch(reservationTime -> reservationTime.getStartAt().equals(startAt));
        }
    }
}
