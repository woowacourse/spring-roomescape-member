package roomescape.theme.service;

import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.theme.domain.Theme;
import roomescape.theme.repository.ThemeRepository;

@Service
@Transactional(readOnly = true)
public class ThemeService {

    private final ThemeRepository themeRepository;

    public ThemeService(final ThemeRepository themeRepository) {
        this.themeRepository = themeRepository;
    }

    @Transactional
    public Theme save(final String name, final String description, final String thumbnailUrl) {
        Theme nonIdTheme = Theme.createNew(name, description, thumbnailUrl);

        if (themeRepository.existsByName(name)) {
            throw new IllegalArgumentException("[ERROR] 테마 이름 중복은 불가능합니다.");
        }

        return themeRepository.save(nonIdTheme);
    }

    @Transactional
    public void deleteById(final long themeId) {
        themeRepository.deleteById(themeId);
    }

    public List<Theme> getAll() {
        return themeRepository.findAll();
    }

    public Theme getById(final long themeId) {
        return themeRepository.findById(themeId)
                .orElseThrow(() -> new IllegalArgumentException("[ERROR] 테마를 찾을 수 없습니다."));
    }

    public List<Theme> getPopularThemes(final int period, final int limit) {
        return themeRepository.findPopularThemes(
                period,
                limit,
                LocalDate.now()
        );
    }

}
