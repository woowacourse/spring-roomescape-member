package roomescape.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.domain.Theme;
import roomescape.dto.ThemeResponse;
import roomescape.exception.NotFoundException;
import roomescape.repository.ThemeDao;

import java.util.List;

@Service
@Transactional
public class ThemeService {
    private final ThemeDao themeDao;

    public ThemeService(ThemeDao themeDao) {
        this.themeDao = themeDao;
    }

    public ThemeResponse create(Theme theme) {
        Theme savedTheme = themeDao.save(theme);
        return ThemeResponse.from(savedTheme);
    }

    @Transactional(readOnly = true)
    public List<ThemeResponse> findAll() {
        List<Theme> themes = themeDao.findAll();
        return themes.stream()
                .map(ThemeResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public ThemeResponse findById(Long id) {
        Theme theme = themeDao.findById(id)
                .orElseThrow(() -> new NotFoundException(id + "에 해당하는 테마가 없습니다."));
        return ThemeResponse.from(theme);
    }

    public void deleteById(Long id) {
        Theme theme = themeDao.findById(id)
                .orElseThrow(() -> new NotFoundException(id + "에 해당하는 테마가 없습니다."));
        themeDao.deleteById(theme.getId());
    }

    @Transactional(readOnly = true)
    public List<ThemeResponse> findAllPopular() {
        List<Theme> allOrderByReservationCountInLastWeek = themeDao.findAllOrderByReservationCountInLastWeek();
        return allOrderByReservationCountInLastWeek.stream()
                .map(ThemeResponse::from)
                .toList();
    }
}
