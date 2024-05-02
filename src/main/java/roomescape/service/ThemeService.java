package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.dao.ThemeDAO;
import roomescape.domain.Theme;
import roomescape.dto.ThemeRequest;

import java.time.LocalDate;
import java.util.List;

@Service
public class ThemeService {
    private final ThemeDAO themeDAO;

    public ThemeService(ThemeDAO themeDAO) {
        this.themeDAO = themeDAO;
    }

    public Theme save(final ThemeRequest themeRequest) {
        final Theme theme = new Theme(themeRequest.name(), themeRequest.description(), themeRequest.thumbnail());
        return themeDAO.insert(theme);
    }

    public List<Theme> findAll() {
        return themeDAO.selectAll();
    }

    public void delete(final Long id) {
        themeDAO.deleteById(id);
    }

    public List<Theme> findTopRanking() {
        LocalDate currentDate = LocalDate.now();
        LocalDate firstDayOfPeriod = currentDate.minusDays(7);
        LocalDate lastDayOfPeriod = currentDate.minusDays(1);
        return themeDAO.findTopRanking(firstDayOfPeriod, lastDayOfPeriod);
    }
}
