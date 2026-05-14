package roomescape.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.domain.theme.Theme;
import roomescape.domain.theme.dto.ThemeCreateRequest;
import roomescape.domain.theme.dto.ThemeResponse;
import roomescape.exception.CustomException;
import roomescape.exception.CustomExceptionCode;
import roomescape.repository.ThemeQueryingDao;
import roomescape.repository.ThemeUpdatingDao;

import java.util.List;

@Transactional(readOnly = true)
@Service
public class ThemeService {

    private final ThemeQueryingDao themeQueryingDao;
    private final ThemeUpdatingDao themeUpdatingDao;

    public ThemeService(ThemeQueryingDao themeQueryingDao, ThemeUpdatingDao themeUpdatingDao) {
        this.themeQueryingDao = themeQueryingDao;
        this.themeUpdatingDao = themeUpdatingDao;
    }

    @Transactional
    public ThemeResponse create(ThemeCreateRequest themeRequest) {
        Long id = themeUpdatingDao.insert(themeRequest);
        return ThemeResponse.from(new Theme(id, themeRequest.getName(), themeRequest.getDescription(), themeRequest.getUrl()));
    }

    public List<ThemeResponse> findAll() {
        return themeQueryingDao.findAllTheme().stream()
                .map(ThemeResponse::from)
                .toList();
    }

    public List<ThemeResponse> findPopularTheme(Integer period, Integer limit) {
        return themeQueryingDao.findAllByTopTheme(period, limit).stream()
                .map(ThemeResponse::from)
                .toList();
    }

    @Transactional
    public void delete(Long id) {
        int count = themeUpdatingDao.delete(id);

        if (count == 0) {
            throw new CustomException(CustomExceptionCode.THEME_NOT_FOUND);
        }
    }
}
