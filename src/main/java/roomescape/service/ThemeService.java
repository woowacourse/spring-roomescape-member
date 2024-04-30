package roomescape.service;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.domain.ThemeRepository;
import roomescape.dto.ThemeResponse;

@Service
public class ThemeService {
    private final ThemeRepository themeRepository;

    public ThemeService(ThemeRepository themeRepository) {
        this.themeRepository = themeRepository;
    }

    public List<ThemeResponse> findAll() {
        return themeRepository.findAll().stream()
                .map(ThemeResponse::from)
                .toList();
    }
}
