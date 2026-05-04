package roomescape.service;

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


    public Theme create(
            ThemeCreateRequest themeCreateRequest
    ) {
        Theme theme = Theme.create(
                themeCreateRequest.name(),
                themeCreateRequest.description(),
                themeCreateRequest.imageUrl()
        );

        return themeRepository.persist(theme);
    }
}
