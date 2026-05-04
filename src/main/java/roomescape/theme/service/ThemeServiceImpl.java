package roomescape.theme.service;

import java.util.List;

import org.springframework.stereotype.Service;
import roomescape.theme.domain.Theme;
import roomescape.theme.exception.ThemeNotFoundException;
import roomescape.theme.repository.ThemeRepository;
import roomescape.theme.service.dto.ThemeSaveServiceDto;

@Service
public class ThemeServiceImpl implements ThemeService {

    private final ThemeRepository themeRepository;

    public ThemeServiceImpl(ThemeRepository themeRepository) {
        this.themeRepository = themeRepository;
    }

    @Override
    public List<Theme> getThemes() {
        return themeRepository.findAll();
    }

    @Override
    public Theme save(ThemeSaveServiceDto theme) {
        Theme newTheme = new Theme(
                theme.getName(),
                theme.getDescription(),
                theme.getImageUrl()
        );
        return themeRepository.save(newTheme);
    }

    @Override
    public void deleteById(Long id) {
        if(!themeRepository.deleteById(id)) {
            throw new ThemeNotFoundException(id);
        }
    }
}