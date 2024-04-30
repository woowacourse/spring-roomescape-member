package roomescape.theme.service;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import roomescape.response.ResponseCode;
import roomescape.theme.dao.ThemeDao;
import roomescape.theme.domain.Theme;
import roomescape.theme.dto.ThemeRequestDto;
import roomescape.theme.dto.ThemeResponseDto;

import java.util.List;

@Service
public class ThemeService {
    private final ThemeDao themeDao;

    public ThemeService(final ThemeDao themeDao) {
        this.themeDao = themeDao;
    }

    public List<ThemeResponseDto> findAll() {
        List<Theme> themes = themeDao.findAll();
        return themes.stream()
                .map(ThemeResponseDto::new)
                .toList();
    }

    public ThemeResponseDto save(final ThemeRequestDto requestDto) {
        final long id = themeDao.save(requestDto.toTheme());
        return new ThemeResponseDto(id, requestDto.name(), requestDto.description(), requestDto.thumbnail());
    }

    public ResponseCode deleteById(final long id) {
        try {
            if (themeDao.deleteById(id) > 0) {
                return ResponseCode.SUCCESS_DELETE;
            }
            return ResponseCode.NOT_FOUND;
        } catch (final DataAccessException dataAccessException) {
            return ResponseCode.FAILED_DELETE;
        }
    }
}
