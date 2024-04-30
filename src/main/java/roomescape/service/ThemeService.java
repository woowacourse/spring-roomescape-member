package roomescape.service;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.dto.ThemeResponse;
import roomescape.dto.ThemeSaveRequest;
import roomescape.model.Theme;
import roomescape.repository.ThemeRepository;

@Service
public class ThemeService {

    private final ThemeRepository themeRepository;

    public ThemeService(final ThemeRepository themeRepository) {
        this.themeRepository = themeRepository;
    }

    public ThemeResponse saveTheme(final ThemeSaveRequest themeSaveRequest) {
        final Theme theme = themeSaveRequest.toTheme();
        final Theme savedTheme = themeRepository.save(theme);
        return new ThemeResponse(savedTheme);
    }

    public List<ThemeResponse> getThemes() {
        return themeRepository.findAll().stream()
                .map(ThemeResponse::new)
                .toList();
    }

    public void deleteTheme(final Long id) {
        // TODO: 테마를 사용하는 예약이 있으면 삭제 불가
        themeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 테마입니다."));
        themeRepository.deleteById(id);
    }
}
