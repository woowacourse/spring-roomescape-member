package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.entity.Theme;
import roomescape.exception.impl.ThemeNotFoundException;
import roomescape.repository.ThemeRepository;

import java.time.LocalDate;
import java.util.List;

@Service
public class ThemeService {

    private final ThemeRepository themeRepository;

    public ThemeService(ThemeRepository themeRepository) {
        this.themeRepository = themeRepository;
    }

    // TODO : DTO 반환 VS 엔티티 반환 고민하기
    // 해보고 경과보기
    public Theme add(
            String name,
            String description,
            String thumbnail
    ) {
        Theme theme = Theme.beforeSave(name, description, thumbnail);
        return themeRepository.save(theme);
    }

    public List<Theme> getThemes() {
        return themeRepository.findAll();
    }

    public void deleteById(long id) {
        boolean exist = themeRepository.existById(id);
        if (!exist) {
            throw new ThemeNotFoundException();
        }
        themeRepository.deleteById(id);
    }

    public List<Theme> getPopularThemes() {
        return themeRepository.findPopularThemes(
                LocalDate.now().minusDays(7),
                LocalDate.now().minusDays(1),
                10
        );
    }
}
