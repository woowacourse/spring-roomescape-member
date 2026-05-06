package roomescape.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.dao.ThemeDao;
import roomescape.domain.Reservation;
import roomescape.domain.Theme;
import roomescape.domain.Time;
import roomescape.domain.vo.Name;
import roomescape.dto.ReservationRequestDto;
import roomescape.dto.ThemeRequestDto;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class ThemeService {
    private final ThemeDao themeDao;

    public ThemeService(ThemeDao themeDao) {
        this.themeDao = themeDao;
    }


    public List<Theme> findAll() {
        return themeDao.findAll();
    }


    public Theme findById(Long id) {
        return themeDao.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 테마입니다."));
    }

    @Transactional
    public Theme create(ThemeRequestDto themeRequest) {
        Name name = new Name(themeRequest.name());
        Theme theme = new Theme(name, themeRequest.thumbnailUrl(), themeRequest.description());
        return themeDao.insert(theme);
    }

    @Transactional
    public void delete(Long id) {
        Theme theme = themeDao.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 테마입니다."));

        themeDao.delete(theme.getId());
    }
}
