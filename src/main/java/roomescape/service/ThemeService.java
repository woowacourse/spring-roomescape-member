package roomescape.service;

import java.time.Clock;
import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.stereotype.Service;
import roomescape.domain.theme.DateRange;
import roomescape.domain.theme.Theme;
import roomescape.domain.theme.ThemeDescription;
import roomescape.domain.theme.ThemeName;
import roomescape.domain.theme.ThemeThumbnail;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ThemeRepository;
import roomescape.service.request.CreateThemeRequest;
import roomescape.service.response.ThemeResponse;

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
            throw new IllegalStateException("이미 예약이 존재해서 테마를 삭제할 수 없습니다.");
        }
        Theme theme = getTheme(id);
        themeRepository.deleteById(theme.getId());
    }

    public ThemeResponse createTheme(final CreateThemeRequest request) {
        Theme theme = themeRepository.save(
                new ThemeName(request.name()),
                new ThemeDescription(request.description()),
                new ThemeThumbnail(request.thumbnail())
        );
        return ThemeResponse.from(theme);
    }

    public List<ThemeResponse> findAllThemes() {
        List<Theme> themes = themeRepository.findAll();
        return ThemeResponse.from(themes);
    }

    public List<ThemeResponse> getWeeklyPopularThemes() {
        DateRange dateRange = DateRange.createLastWeekRange(clock);
        List<Theme> themes = themeRepository.findPopularThemeDuringAWeek(10L, dateRange);
        return ThemeResponse.from(themes);
    }

    private Theme getTheme(final Long id) {
        return themeRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("해당 테마가 존재하지 않습니다."));
    }
}
