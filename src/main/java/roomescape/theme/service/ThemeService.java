package roomescape.theme.service;

import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.stereotype.Service;
import roomescape.theme.dto.request.CreateThemeRequest;
import roomescape.theme.dto.response.CreateThemeResponse;
import roomescape.theme.dto.response.FindPopularThemesResponse;
import roomescape.theme.dto.response.FindThemeResponse;
import roomescape.theme.model.Theme;
import roomescape.theme.repository.ThemeRepository;

@Service
public class ThemeService {

    private final ThemeRepository themeRepository;

    public ThemeService(final ThemeRepository themeRepository) {
        this.themeRepository = themeRepository;
    }

    public CreateThemeResponse createTheme(CreateThemeRequest createThemeRequest) {
        Theme theme = themeRepository.save(createThemeRequest.toTheme());
        return CreateThemeResponse.of(theme);
    }

    public List<FindThemeResponse> getThemes() {
        List<Theme> themes = themeRepository.findAll();
        return themes.stream()
                .map(FindThemeResponse::of)
                .toList();
    }

    public List<FindPopularThemesResponse> getPopularThemes() {
        return themeRepository.findOrderByReservation().stream()
                .map(FindPopularThemesResponse::of)
                .toList();
    }

    public void deleteById(final Long id) {
        themeRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("해당하는 테마가 존재하지 않습니다."));
        themeRepository.deleteById(id);
    }
}
