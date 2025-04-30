package roomescape.service;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.controller.dto.ThemeRequest;
import roomescape.controller.dto.ThemeResponse;
import roomescape.repository.ThemeDao;
import roomescape.service.reservation.Theme;
import roomescape.service.reservation.ThemeName;

@Service
public class ThemeService {

    private final ThemeDao themeDao;

    public ThemeService(final ThemeDao themeDao) {
        this.themeDao = themeDao;
    }

    public ThemeResponse createTheme(final ThemeRequest themeRequest) {
        final ThemeName name = new ThemeName(themeRequest.name());
        if (themeDao.isExists(name)) {
            throw new IllegalArgumentException("해당 이름의 테마는 이미 존재합니다.");
        }
        Theme savedTheme = themeDao.save(themeRequest.convertToTheme());
        return new ThemeResponse(savedTheme);
    }

    public List<ThemeResponse> findAll() {
        List<Theme> themes = themeDao.findAll();
        return themes.stream()
                .map(ThemeResponse::new)
                .toList();
    }
}
