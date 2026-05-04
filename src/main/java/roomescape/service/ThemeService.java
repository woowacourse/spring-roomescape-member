package roomescape.service;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.controller.dto.ThemeCreateRequest;
import roomescape.domain.Theme;
import roomescape.repository.ThemeRepository;

@Service
public class ThemeService {
    private final ThemeRepository themeRepository;

    public ThemeService(ThemeRepository themeRepository) {
        this.themeRepository = themeRepository;
    }

    public Theme find(long themeId) {
        return themeRepository.findById(themeId);
    }

    public List<Theme> findAll() {
        return themeRepository.findAll();
    }

    public Theme create(ThemeCreateRequest request) {
        Theme theme = Theme.of(request.getName(), request.getDescription(), request.getThumbnailUrl());
        return themeRepository.save(theme);
    }

    public void delete(long id) {
        themeRepository.deleteById(id);
    }
}
