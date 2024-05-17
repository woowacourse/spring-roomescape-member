package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.repository.ThemeRepository;
import roomescape.domain.Theme;
import roomescape.dto.request.ThemeRequest;

import java.time.LocalDate;
import java.util.List;

@Service
public class ThemeService {

    private static final int START_DAY = 7;
    private static final int END_DAY = 1;

    private final ThemeRepository themeRepository;

    public ThemeService(ThemeRepository themeRepository) {
        this.themeRepository = themeRepository;
    }

    public Theme save(ThemeRequest themeRequest) {
        Theme theme = new Theme(themeRequest.name(), themeRequest.description(), themeRequest.thumbnail());
        return themeRepository.insert(theme);
    }

    public List<Theme> findAll() {
        return themeRepository.selectAll();
    }

    public void delete(Long id) {
        themeRepository.deleteById(id);
    }

    public List<Theme> findTopRanking() {
        LocalDate currentDate = LocalDate.now();
        LocalDate firstDayOfPeriod = currentDate.minusDays(START_DAY);
        LocalDate lastDayOfPeriod = currentDate.minusDays(END_DAY);
        return themeRepository.findTopRanking(firstDayOfPeriod, lastDayOfPeriod);
    }
}
