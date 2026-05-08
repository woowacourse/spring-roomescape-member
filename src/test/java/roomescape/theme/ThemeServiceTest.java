package roomescape.theme;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.repository.MemoryReservationRepository;
import roomescape.theme.domain.Theme;
import roomescape.theme.repository.ThemeRepository;
import roomescape.theme.service.ThemeService;

class ThemeServiceTest {

    @Test
    @DisplayName("예약이 존재하는 테마는 삭제할 수 없다")
    void deleteById() {
        TestThemeRepository themeRepository = new TestThemeRepository();
        MemoryReservationRepository reservationRepository = new MemoryReservationRepository();
        ThemeService themeService = new ThemeService(themeRepository, reservationRepository);

        Theme theme = themeRepository.save(Theme.createNew("미술관의 밤", "추리 테마", "https://example.com/theme.png"));
        reservationRepository.save(Reservation.of(
                1L,
                "쿠다",
                java.time.LocalDate.parse("2026-08-06"),
                theme,
                roomescape.reservationtime.domain.ReservationTime.of(1L, java.time.LocalTime.parse("10:00"))
        ));

        assertThrows(IllegalArgumentException.class, () -> themeService.deleteById(theme.getId()));
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
}
