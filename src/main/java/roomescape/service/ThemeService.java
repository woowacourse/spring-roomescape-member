package roomescape.service;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.domain.Theme;
import roomescape.dto.request.CreateThemeRequest;
import roomescape.exception.InvalidThemeException;
import roomescape.repository.ThemeRepository;

@Service
public class ThemeService {

    private final ThemeRepository themeRepository;

    public ThemeService(ThemeRepository themeRepository) {
        this.themeRepository = themeRepository;
    }

    public Theme addTheme(CreateThemeRequest request) {
        Theme theme = request.toTheme();
        return themeRepository.add(theme);
    }

    public List<Theme> findAll() {
        return themeRepository.findAll();
    }

    public Theme getThemeById(long id) {
        return themeRepository.findById(id)
                .orElseThrow(() -> new InvalidThemeException("존재하지 않는 테마입니다."));
    }

    public void deleteThemeById(long id) {
        if (themeRepository.existsReservationByThemeId(id)) {
            throw new InvalidThemeException("예약이 존재하는 테마는 삭제할 수 없습니다.");
        }
        themeRepository.deleteById(id);
    }
}
