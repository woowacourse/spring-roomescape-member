package roomescape.theme.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.theme.domain.exception.ThemeNotFoundException;
import roomescape.theme.domain.Theme;
import roomescape.theme.domain.ThemeRepository;
import roomescape.theme.presentation.dto.ThemeRequest;
import roomescape.theme.presentation.dto.ThemeResponse;

@Service
@Transactional
@RequiredArgsConstructor
public class ThemeService {

    private final ThemeRepository repository;

    public ThemeResponse addTheme(ThemeRequest request) {
        Theme theme = repository.save(ThemeRequest.toEntity(request));
        return ThemeResponse.from(theme);
    }

    public void deleteTheme(Long id) {
        if (!repository.existsThemeById(id)) {
            throw new ThemeNotFoundException("존재하지 않는 테마입니다.");
        }
        repository.delete(id);
    }
}
