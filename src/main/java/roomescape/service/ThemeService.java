package roomescape.service;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.dao.ThemeDao;
import roomescape.domain.Theme;
import roomescape.dto.request.ThemeCreateRequest;
import roomescape.dto.response.PopularThemeResponse;
import roomescape.dto.response.ThemeResponse;
import roomescape.exception.ReservationTimeInUseException;
import roomescape.exception.ThemeNotFoundException;
import roomescape.exception.UnauthorizedException;

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
        validateAdmin(request.userName());
        Long id = themeDao.insertTheme(request.name(), request.description(), request.imgUrl());
        return ThemeResponse.from(themeDao.findById(id));
    }

    @Transactional
    public void deleteTheme(Long id, String userName) {
        try {
            validateAdmin(userName);
            int deleteCount = themeDao.delete(id);
            validateDelete(deleteCount);
        } catch (DataIntegrityViolationException e) {
            throw new ReservationTimeInUseException();
        }
    }

    public List<ThemeResponse> getThemes() {
        return ThemeResponse.from(themeDao.findAllThemes());
    }

    public List<PopularThemeResponse> getPopularThemes(LocalDate from, LocalDate to) {
        return PopularThemeResponse.toDto(themeDao.findPopularThemes(from, to));
    }

    private void validateDelete(int deleteCount) {
        if (deleteCount == 0) {
            throw new ThemeNotFoundException();
        }
    }

    private void validateAdmin(String userName) {
        if (!userName.equals("ADMIN")) {
            throw new UnauthorizedException();
        }
    }
}
