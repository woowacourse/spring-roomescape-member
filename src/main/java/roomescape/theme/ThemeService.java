package roomescape.theme;

import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.exception.ErrorCode;
import roomescape.exception.RoomescapeException;
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

    public ThemeService(ThemeRepository themeRepository) {
        this.themeRepository = themeRepository;
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

    public ThemesResponse read(int page, int size) {
        List<ThemeResponse> themesResponse = themeRepository.findAll(page, size).stream()
                .map(ThemeResponse::from)
                .toList();

        return ThemesResponse.from(themesResponse);
    }

    @Transactional
    public void delete(Long id) {
        themeRepository.findById(id).orElseThrow(() -> new RoomescapeException(ErrorCode.THEME_NOT_FOUND));
        themeRepository.deleteById(id);
    }

    public ThemesResponse readPopularThemes(LocalDate now) {
        LocalDate start = now.minusDays(POPULAR_PERIOD);
        LocalDate end = now.minusDays(POPULAR_OFFSET);
        List<Theme> themes = themeRepository.findPopularThemes(start, end, POPULAR_LIMIT);

        List<ThemeResponse> themesResponse = themes.stream()
                .map(ThemeResponse::from)
                .toList();

        return ThemesResponse.from(themesResponse);
    }
}
