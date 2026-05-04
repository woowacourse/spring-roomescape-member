package roomescape.theme.service;

import org.springframework.stereotype.Service;
import roomescape.theme.entity.Theme;
import roomescape.theme.payload.ThemeRequest;
import roomescape.theme.repository.ThemeRepository;

@Service
public class ThemeService {

    private final ThemeRepository themeRepository;

    public ThemeService(ThemeRepository themeRepository) {
        this.themeRepository = themeRepository;
    }

    public Theme save(ThemeRequest request) {
        return themeRepository.save(request.name(), request.description(), request.thumbnailUrl());
    }

}
