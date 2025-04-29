package roomescape.domain.reservation.service;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.domain.reservation.dto.ThemeRequest;
import roomescape.domain.reservation.dto.ThemeResponse;
import roomescape.domain.reservation.entity.Theme;
import roomescape.domain.reservation.repository.ThemeRepository;

@Service
public class ThemeService {

    private final ThemeRepository themeRepository;

    public ThemeService(ThemeRepository themeRepository) {
        this.themeRepository = themeRepository;
    }

    public List<ThemeResponse> getAll() {
        return themeRepository.findAll()
                .stream()
                .map(ThemeResponse::from)
                .toList();
    }

    public ThemeResponse create(ThemeRequest request) {
        Theme theme = themeRepository.save(Theme.withoutId(request.name(), request.description(), request.thumbnail()));
        return ThemeResponse.from(theme);
    }

    public void delete(Long id) {
        themeRepository.deleteById(id);
    }

}
