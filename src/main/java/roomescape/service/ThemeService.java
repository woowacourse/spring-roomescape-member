package roomescape.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.domain.theme.Theme;
import roomescape.domain.theme.ThemeRequest;
import roomescape.domain.theme.ThemeResponse;
import roomescape.repository.ThemeQueryingDao;
import roomescape.repository.ThemeUpdatingDao;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class ThemeService {

    private final ThemeQueryingDao themeQueryingDao;
    private final ThemeUpdatingDao themeUpdatingDao;

    public ThemeService(ThemeQueryingDao themeQueryingDao, ThemeUpdatingDao themeUpdatingDao) {
        this.themeQueryingDao = themeQueryingDao;
        this.themeUpdatingDao = themeUpdatingDao;
    }

    @Transactional
    public ThemeResponse create(ThemeRequest theme) {
        Long id = themeUpdatingDao.insert(theme);
        return new ThemeResponse(id, theme.getName(), theme.getDescription(), theme.getUrl());
    }

    public List<ThemeResponse> findAll() {
        return themeQueryingDao.findAllTheme().stream()
                .map(theme -> new ThemeResponse(theme.getId(), theme.getName(), theme.getDescription(), theme.getUrl()))
                .toList();
    }

    @Transactional
    public void delete(Long id) {
        themeUpdatingDao.delete(id);
    }
}
