package roomescape.theme.service;

import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.common.Dao;
import roomescape.reservation.domain.Reservation;
import roomescape.theme.dao.ThemeDao;
import roomescape.theme.domain.Theme;
import roomescape.theme.dto.BestThemeResponse;
import roomescape.theme.dto.ThemeRequest;
import roomescape.theme.dto.ThemeResponse;

@Service
public class ThemeService {
    private final ThemeDao themeDao;

    public ThemeService(ThemeDao themeDao) {
        this.themeDao = themeDao;
    }

    public List<ThemeResponse> findAll() {
        return themeDao.findAll().stream()
                .map(theme -> new ThemeResponse(
                        theme.getId(),
                        theme.getName(),
                        theme.getDescription(),
                        theme.getThumbnail()))
                .toList();
    }

    public ThemeResponse add(ThemeRequest themeRequest) {
        if (isAlreadyExisted(themeRequest)) {
            throw new IllegalArgumentException("[ERROR] 이미 테마가 존재합니다.");
        }

        Theme theme = new Theme(null, themeRequest.name(), themeRequest.description(), themeRequest.thumbnail());
        Theme savedTheme = themeDao.add(theme);
        return new ThemeResponse(savedTheme.getId(),
                savedTheme.getName(),
                savedTheme.getDescription(),
                savedTheme.getThumbnail());
    }

    private boolean isAlreadyExisted(ThemeRequest themeRequest) {
        List<Theme> themes = themeDao.findAll();
        return themes.stream()
                .anyMatch(
                        theme -> theme.getName().equals(themeRequest.name())
                );
    }

    public void deleteById(Long id) {
        themeDao.findById(id).orElseThrow(() -> new IllegalArgumentException("[ERROR] 해당 테마 아이디는 존재하지 않습니다"));
        themeDao.deleteById(id);
    }

    public List<BestThemeResponse> findBest() {
        List<Theme> bestThemes = themeDao.findBest(LocalDate.now().minusDays(7), LocalDate.now().minusDays(1));
        return bestThemes.stream()
                .map(theme -> new BestThemeResponse(theme.getName(), theme.getDescription(), theme.getThumbnail()))
                .toList();
    }
}
