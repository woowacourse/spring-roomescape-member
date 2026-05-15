package roomescape.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.dao.ThemeDao;
import roomescape.dto.request.ThemeCreateRequest;
import roomescape.dto.response.PopularThemeResponse;
import roomescape.dto.response.ThemeResponse;
import roomescape.exception.ThemeNotFoundException;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class ThemeService {
    private final ThemeDao themeDao;

    public ThemeService(ThemeDao themeDao) {
        this.themeDao = themeDao;
    }

    @Transactional
    public ThemeResponse createTheme(ThemeCreateRequest request) {
        Long id = themeDao.insertTheme(request.name(), request.description(), request.imgUrl());
        return ThemeResponse.from(themeDao.findById(id));
    }

    @Transactional
    public void deleteTheme(Long id) {
        int deleteCount = themeDao.delete(id);
        validateDelete(deleteCount);
    }

    public List<ThemeResponse> getThemes() {
        return ThemeResponse.from(themeDao.findAllThemes());
    }

    public List<PopularThemeResponse> getPopularThemes(LocalDate from, LocalDate to) {
        return PopularThemeResponse.toDto(themeDao.findPopularThemes(from, to));
    }

    private void validateDelete(int deleteCount) {
        if (deleteCount == 0) {
            throw new ThemeNotFoundException("해당 테마를 찾을 수 없습니다.");
        }
    }
}
