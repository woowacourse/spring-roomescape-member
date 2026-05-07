package roomescape.service;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.domain.Theme;
import roomescape.repository.ThemeRepository;

@Service
@Transactional(readOnly = true)
public class ThemeService {

    private final ThemeRepository themeRepository;

    public ThemeService(ThemeRepository themeRepository) {
        this.themeRepository = themeRepository;
    }

    public List<Theme> findAll() {
        return themeRepository.findAll();
    }

    @Transactional
    public Theme create(String name, String description, String thumbnail) {
        Theme theme = new Theme(null, name, description, thumbnail);
        Long id = themeRepository.insert(theme);
        return themeRepository.findBy(id)
                .orElseThrow(() -> new IllegalArgumentException("[ERROR] 존재하지 않는 테마입니다."));
    }

    @Transactional
    public void delete(Long id) {
        if (!themeRepository.existsById(id)) {
            throw new NoSuchElementException("[ERROR] 존재하지 않는 ID입니다.");
        }
        themeRepository.delete(id);
    }

    public List<Theme> findWeeklyTopTen() {
        LocalDate startDate = LocalDate.now().minusWeeks(1);
        LocalDate endDate = startDate.plusDays(6);
        return themeRepository.findPopular(startDate, endDate, 10);
    }
}
