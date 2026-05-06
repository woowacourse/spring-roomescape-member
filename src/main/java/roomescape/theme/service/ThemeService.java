package roomescape.theme.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import roomescape.theme.domain.Theme;
import roomescape.theme.dto.ThemeCreateRequest;
import roomescape.theme.dto.ThemeResponse;
import roomescape.theme.exception.ThemeException;
import roomescape.theme.repository.ThemeRepository;

@RequiredArgsConstructor
@Service
public class ThemeService {

    private final ThemeRepository themeRepository;

    public Theme findById(Long id) {
        return themeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("[ERROR] 존재하지 않는 테마 입니다."));
    }

    public List<ThemeResponse> findAll() {
        List<Theme> themes = themeRepository.findAll();

        return themes.stream().map(ThemeResponse::from)
                .toList();
    }

    public ThemeResponse save(ThemeCreateRequest request) {
        Theme theme = request.toEntity();
        validateDuplicateTheme(theme);

        return ThemeResponse.from(themeRepository.save(theme));
    }

    public int delete(long id) {
        return themeRepository.delete(id);
    }

    private void validateDuplicateTheme(Theme theme) {
        if (themeRepository.existsByNameAndDescription(theme)) {
            throw new ThemeException("[ERROR] 이름과 설명이 같은 테마가 이미 존재합니다.");
        }
    }
}
