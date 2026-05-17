package roomescape.theme;

import java.time.Clock;
import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.exception.AlreadyInUseException;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.theme.dto.PageThemesResponse;
import roomescape.theme.dto.ThemeRequest;
import roomescape.theme.dto.ThemeResponse;
import roomescape.theme.dto.ThemesResponse;
import roomescape.theme.repository.ThemeRepository;

@Service
public class ThemeService {

    private static final int POPULAR_PERIOD = 7;
    private static final int POPULAR_OFFSET = 1;
    private static final int POPULAR_LIMIT = 10;

    private final ThemeRepository themeRepository;
    private final ReservationRepository reservationRepository;
    private final Clock clock;

    public ThemeService(ThemeRepository themeRepository, ReservationRepository reservationRepository, Clock clock) {
        this.themeRepository = themeRepository;
        this.reservationRepository = reservationRepository;
        this.clock = clock;
    }

    @Transactional
    public ThemeResponse create(ThemeRequest themeRequest) {
        Theme theme = new Theme(
                themeRequest.name(),
                themeRequest.description(),
                themeRequest.thumbnail()
        );

        Theme saved = themeRepository.save(theme);
        return ThemeResponse.from(saved);
    }

    public PageThemesResponse read(int page, int size) {
        List<ThemeResponse> themesResponse = themeRepository.findAll(page, size).stream()
                .map(ThemeResponse::from)
                .toList();

        boolean hasNext = themesResponse.size() > size;
        if (hasNext) {
            themesResponse = themesResponse.subList(0, size);
        }

        return PageThemesResponse.from(themesResponse, page, size, hasNext);
    }

    @Transactional
    public void delete(Long id) {
        if (reservationRepository.existsByThemeId(id)) {
            throw new AlreadyInUseException("테마에 해당하는 예약이 있습니다.");
        }
        themeRepository.deleteById(id);
    }

    public ThemesResponse readPopularThemes() {
        LocalDate now = LocalDate.now(clock);
        LocalDate start = now.minusDays(POPULAR_PERIOD);
        LocalDate end = now.minusDays(POPULAR_OFFSET);
        List<Theme> themes = themeRepository.findPopularThemes(start, end, POPULAR_LIMIT);

        List<ThemeResponse> themesResponse = themes.stream()
                .map(ThemeResponse::from)
                .toList();

        return ThemesResponse.from(themesResponse);
    }
}
