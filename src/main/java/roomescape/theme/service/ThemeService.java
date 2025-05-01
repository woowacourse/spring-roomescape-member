package roomescape.theme.service;

import java.time.Clock;
import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.stereotype.Service;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.theme.controller.request.CreateThemeRequest;
import roomescape.theme.controller.response.ThemeResponse;
import roomescape.theme.domain.Theme;
import roomescape.theme.repository.ThemeRepository;

@Service
public class ThemeService {

    private final ThemeRepository themeRepository;
    private final ReservationRepository reservationRepository;
    private final Clock clock;

    public ThemeService(
            ThemeRepository themeRepository,
            ReservationRepository reservationRepository,
            Clock clock
    ) {
        this.themeRepository = themeRepository;
        this.reservationRepository = reservationRepository;
        this.clock = clock;
    }

    public void deleteThemeById(final Long id) {
        if (reservationRepository.existReservationByThemeId(id)) {
            throw new IllegalStateException("[ERROR] 이미 예약이 존재해서 테마를 삭제할 수 없습니다.");
        }
        Theme theme = getTheme(id);
        themeRepository.deleteById(theme.getId());
    }

    public ThemeResponse createTheme(final CreateThemeRequest request) {
        Theme theme = themeRepository.save(request.name(), request.description(), request.thumbnail());
        return ThemeResponse.from(theme);
    }

    public List<ThemeResponse> getAllThemes() {
        List<Theme> themes = themeRepository.findAll();
        return ThemeResponse.from(themes);
    }

    public List<ThemeResponse> getWeeklyPopularThemes() {
        List<Theme> themes = themeRepository.findPopularThemeDuringAWeek(10, LocalDate.now(clock));
        return ThemeResponse.from(themes);
    }

    private Theme getTheme(final Long id) {
        return themeRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("[ERROR] 해당 테마가 존재하지 않습니다."));
    }
}
