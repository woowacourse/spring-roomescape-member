package roomescape.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import roomescape.domain.Theme;
import roomescape.domain.repository.ThemeRepository;
import roomescape.dto.request.ThemeRequest;
import roomescape.dto.response.ThemeResponse;
import roomescape.exception.ExistedThemeException;

@Service
public class ThemeService {

    private final ThemeRepository themeRepository;

    public ThemeService(ThemeRepository themeRepository) {
        this.themeRepository = themeRepository;
    }

    public ThemeResponse create(ThemeRequest themeRequest) {
        Optional<Theme> optionalTheme = themeRepository.findByName(themeRequest.name());
        if (optionalTheme.isPresent()) {
            throw new ExistedThemeException();
        }

        Theme theme = Theme.createWithoutId(themeRequest.name(), themeRequest.description(), themeRequest.thumbnail());
        Theme themeWithId = themeRepository.create(theme);
        return new ThemeResponse(themeWithId.getId(), themeWithId.getName(), themeWithId.getDescription(),
                themeWithId.getThumbnail());
    }

    public List<ThemeResponse> findAll() {
        List<Theme> themes = themeRepository.findAll();
        return themes.stream()
                .map(theme ->
                        new ThemeResponse(
                                theme.getId(),
                                theme.getName(),
                                theme.getDescription(),
                                theme.getThumbnail()
                        )
                )
                .toList();
    }

    public int delete(long id) {
        return themeRepository.delete(id);
    }

    public List<ThemeResponse> getTop10MostReservedThemesInLast7Days() {
        LocalDate startDate = LocalDate.now().minusDays(8);
        List<Theme> themes = themeRepository.findTopNReservedThemesBetween(10, startDate, LocalDate.now());

        return themes.stream().map(theme -> new ThemeResponse(theme.getId(), theme.getName(), theme.getDescription(),
                theme.getThumbnail())).toList();
    }
}
