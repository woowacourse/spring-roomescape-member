package roomescape.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.domain.Theme;
import roomescape.repository.ThemeRepository;

import java.util.List;

@Service
public class ThemeService {
    private final ThemeRepository themeRepository;

    public ThemeService(ThemeRepository themeRepository) {
        this.themeRepository = themeRepository;
    }

    @Transactional
    public Theme create(String name, String description, String thumbnail) {
        Theme theme = new Theme(name, description, thumbnail);

        return themeRepository.save(theme);
    }

    @Transactional(readOnly = true)
    public List<Theme> list(){
        return themeRepository.findAll();
    }

    @Transactional
    public void delete(Long id) {
        themeRepository.deleteById(id);
    }
}
