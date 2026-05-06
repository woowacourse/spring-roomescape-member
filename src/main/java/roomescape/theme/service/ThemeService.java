package roomescape.theme.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import roomescape.theme.domain.Theme;
import roomescape.theme.dto.ThemeCreateRequest;
import roomescape.theme.dto.ThemeResponse;
import roomescape.theme.repository.ThemeRepository;

@RequiredArgsConstructor
@Service
public class ThemeService {

    private final ThemeRepository themeRepository;

    public ThemeResponse saveTheme(ThemeCreateRequest request) {
        Theme theme = request.toEntity();
        validateDuplicateTheme(theme);

        return ThemeResponse.from(themeRepository.save(theme));
    }

    private void validateDuplicateTheme(Theme theme) {
        if (themeRepository.existsByNameAndDescription(theme)) {
            throw new IllegalArgumentException();
        }
    }

    public int delete(long id) {
        return themeRepository.delete(id);
    }

    public List<ThemeResponse> findAllThemes() {
        List<Theme> themes = themeRepository.findAll();

        return themes.stream().map(ThemeResponse::from)
                .toList();
    }

    public Theme findById(Long id) {
        return themeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("[ERROR] 존재하지 않는 테마 입니다."));
    }
}
