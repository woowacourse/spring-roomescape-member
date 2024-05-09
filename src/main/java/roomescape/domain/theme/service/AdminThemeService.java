package roomescape.domain.theme.service;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.domain.theme.domain.Theme;
import roomescape.domain.theme.dto.ThemeAddRequest;
import roomescape.global.exception.ClientIllegalArgumentException;
import roomescape.domain.theme.repository.ThemeRepository;

@Service
public class AdminThemeService {

    private final ThemeRepository themeRepository;

    public AdminThemeService(ThemeRepository themeRepository) {
        this.themeRepository = themeRepository;
    }

    public List<Theme> findAllTheme() {
        return themeRepository.findAll();
    }

    public Theme addTheme(ThemeAddRequest themeAddRequest) {
        Theme theme = themeAddRequest.toEntity();
        return themeRepository.insert(theme);
    }

    public void removeTheme(Long id) {
        if (themeRepository.findById(id).isEmpty()) {
            throw new ClientIllegalArgumentException("해당 id를 가진 테마가 존재하지 않습니다.");
        }
        themeRepository.deleteById(id);
    }
}
