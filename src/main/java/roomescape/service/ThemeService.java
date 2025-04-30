package roomescape.service;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.dto.ThemeRequest;
import roomescape.dto.ThemeResponse;
import roomescape.entity.Theme;
import roomescape.repository.ThemeRepository;

@Service
@Transactional(readOnly = true)
public class ThemeService {

    private final ThemeRepository repository;

    public ThemeService(ThemeRepository repository) {
        this.repository = repository;
    }

    public List<ThemeResponse> readAllTheme() {
        return repository.findAll().stream()
                .map(ThemeResponse::from)
                .toList();
    }

    @Transactional
    public ThemeResponse postTheme(ThemeRequest request) {
        Theme newTheme = repository.save(request.toEntity());
        return ThemeResponse.from(newTheme);
    }

    @Transactional
    public void deleteTheme(long id) {
        repository.deleteById(id);
    }
}
