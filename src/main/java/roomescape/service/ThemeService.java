package roomescape.service;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.dao.ThemeRepository;
import roomescape.domain.Theme;
import roomescape.exception.InvalidReservationException;
import roomescape.service.dto.ThemeRequest;
import roomescape.service.dto.ThemeResponse;

@Service
public class ThemeService {
    private final ThemeRepository themeRepository;

    public ThemeService(ThemeRepository themeRepository) {
        this.themeRepository = themeRepository;
    }

    public ThemeResponse create(ThemeRequest themeRequest) {
        validateDuplicated(themeRequest);
        Theme theme = new Theme(themeRequest.name(), themeRequest.description(), themeRequest.thumbnail());
        Theme newTheme = themeRepository.save(theme);
        return new ThemeResponse(newTheme);
    }

    private void validateDuplicated(ThemeRequest themeRequest) {
        if (themeRepository.existsByName(themeRequest.name())) {
            throw new InvalidReservationException("이미 존재하는 테마 이름입니다.");
        }
    }

    public List<ThemeResponse> findAll() {
        return themeRepository.findAll().stream()
                .map(ThemeResponse::new)
                .toList();
    }

    public void deleteById(long id) {
        themeRepository.deleteById(id);
    }
}
