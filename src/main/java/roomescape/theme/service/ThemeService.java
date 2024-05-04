package roomescape.theme.service;

import org.springframework.stereotype.Service;
import roomescape.theme.dao.ThemeDao;
import roomescape.theme.domain.Theme;
import roomescape.theme.dto.ThemeRequestDto;
import roomescape.theme.dto.ThemeResponseDto;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ThemeService {
    private final ThemeDao themeDao;

    public ThemeService(final ThemeDao themeDao) {
        this.themeDao = themeDao;
    }

    public List<ThemeResponseDto> findAll() {
        final List<Theme> themes = themeDao.findAll();
        return themes.stream()
                .map(ThemeResponseDto::new)
                .toList();
    }

    public ThemeResponseDto save(final ThemeRequestDto requestDto) {
        final long id = themeDao.save(requestDto.toTheme());
        return new ThemeResponseDto(id, requestDto.name(), requestDto.description(), requestDto.thumbnail());
    }

    public void deleteById(final long id) {
        final int deleteCount = themeDao.deleteById(id);
        if (deleteCount == 0) {
            throw new NoSuchElementException(id + "를 아이디로 갖는 테마가 존재하지 않습니다.");
        }
    }

    public List<ThemeResponseDto> findPopular() {
        final LocalDate today = LocalDate.now();

        final List<Theme> themes = themeDao.findPopular(today.minusWeeks(1).toString(), today.minusDays(1).toString());
        return themes.stream()
                .map(ThemeResponseDto::new)
                .toList();
    }
}
