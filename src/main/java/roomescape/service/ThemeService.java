package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.dao.ThemeDAO;
import roomescape.domain.Theme;
import roomescape.dto.ThemeRequest;

import java.time.LocalDate;
import java.util.List;

@Service
public class ThemeService {

    private static final int START_DAY = 7;
    private static final int END_DAY = 1;

    private final ThemeDAO themeDAO;

    public ThemeService(ThemeDAO themeDAO) {
        this.themeDAO = themeDAO;
    }

    public Theme save(ThemeRequest themeRequest) {
        Theme theme = new Theme(themeRequest.name(), themeRequest.description(), themeRequest.thumbnail());
        return themeDAO.insert(theme);
    }

    public List<Theme> findAll() {
        return themeDAO.selectAll();
    }

    public void delete(Long id) {
        themeDAO.deleteById(id);
    }

    public List<Theme> findTopRanking() {
        LocalDate currentDate = LocalDate.now();
        LocalDate firstDayOfPeriod = currentDate.minusDays(START_DAY);
        LocalDate lastDayOfPeriod = currentDate.minusDays(END_DAY);
        return themeDAO.findTopRanking(firstDayOfPeriod, lastDayOfPeriod);
    }
}
