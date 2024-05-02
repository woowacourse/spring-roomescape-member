package roomescape.service;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import roomescape.domain.Theme;
import roomescape.dto.ThemeResponse;
import roomescape.dto.ThemeSaveRequest;
import roomescape.exception.IllegalThemeException;
import roomescape.mapper.ThemeMapper;
import roomescape.repository.ReservationDao;
import roomescape.repository.ThemeDao;

import java.util.List;
import java.util.Optional;

@Service
public class ThemeService {

    private final ThemeMapper themeMapper = new ThemeMapper();
    private final ThemeDao themeDao;
    private final ReservationDao reservationDao;

    public ThemeService(ThemeDao themeDao, ReservationDao reservationDao) {
        this.themeDao = themeDao;
        this.reservationDao = reservationDao;
    }

    public List<ThemeResponse> findAllThemes() {
        List<Theme> themes = themeDao.findAll();
        return themes.stream()
                .map(themeMapper::mapToResponse)
                .toList();
    }

    public Theme findThemeById(Long id) {
        if (id == null) {
            throw new IllegalThemeException("[ERROR] 유효하지 않은 형식의 테마입니다.");
        }
        Optional<Theme> optionalTheme = themeDao.findById(id);
        if (optionalTheme.isEmpty()) {
            throw new IllegalThemeException("[ERROR] 테마를 찾을 수 없습니다");
        }
        return optionalTheme.get();
    }

    public List<ThemeResponse> findBestThemes() {
        List<Theme> themesByDescOrder = themeDao.findThemesByDescOrder();
        return themesByDescOrder.stream()
                .map(themeMapper::mapToResponse)
                .toList();
    }

    public ThemeResponse save(ThemeSaveRequest request) {
        Theme theme = themeMapper.mapToTheme(request);

        Long saveId = themeDao.save(theme);
        return themeMapper.mapToResponse(saveId, theme);
    }

    public void deleteThemeById(Long id) {
        try {
            themeDao.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalThemeException("[ERROR] 예약이 존재하는 테마는 삭제할 수 없습니다.");
        }
    }
}
