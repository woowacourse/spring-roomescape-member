package roomescape.theme.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.theme.dto.ThemeRequest;
import roomescape.theme.dto.ThemeResponse;
import roomescape.theme.model.Theme;
import roomescape.theme.repository.ThemeRepository;

@Service
@Transactional(readOnly = true)
public class ThemeService {

    private final ThemeRepository themeRepository;

    public ThemeService(ThemeRepository themeRepository) {
        this.themeRepository = themeRepository;
    }

    @Transactional
    public ThemeResponse create(ThemeRequest request) {
        Theme theme = new Theme(request.name(), request.description(), request.imageUrl());
        Long id = themeRepository.create(theme);
        return ThemeResponse.from(new Theme(id, request.name(), request.description(), request.imageUrl()));
    }

    public void delete(Long id) {
        themeRepository.delete(id);
    }
}
