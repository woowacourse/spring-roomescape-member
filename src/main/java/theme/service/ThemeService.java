package theme.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import theme.domain.Theme;
import theme.repository.ThemeRepository;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class ThemeService {

    private final ThemeRepository themeRepository;

    public ThemeService(ThemeRepository themeRepository) {
        this.themeRepository = themeRepository;
    }

    @Transactional
    public Theme save(ThemeCommand command) {
        Theme theme = Theme.create(command.name(), command.description(), command.thumbnailUrl());
        return themeRepository.save(theme);
    }

    @Transactional
    public void deleteById(Long id) {
        themeRepository.deleteById(id);
    }

    public List<Theme> findAll() {
        return themeRepository.findAll();
    }
}
