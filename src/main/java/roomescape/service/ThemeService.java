package roomescape.service;

import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.common.exception.ConflictException;
import roomescape.common.exception.NotFoundException;
import roomescape.dao.ReservationDao;
import roomescape.dao.ThemeDao;
import roomescape.domain.Theme;
import roomescape.domain.vo.Name;
import roomescape.dto.request.PopularThemeRequestDto;
import roomescape.dto.request.ThemeRequestDto;
import roomescape.dto.response.AvailableTimeResponseDto;

@Service
@Transactional(readOnly = true)
public class ThemeService {
    private final ThemeDao themeDao;
    private final ReservationDao reservationDao;

    public ThemeService(ThemeDao themeDao, ReservationDao reservationDao) {
        this.themeDao = themeDao;
        this.reservationDao = reservationDao;
    }

    public List<Theme> findAll() {
        return themeDao.findAll();
    }


    public Theme findById(Long id) {
        return themeDao.findById(id)
                .orElseThrow(() -> new NotFoundException("존재하지 않는 테마입니다."));
    }

    @Transactional
    public Theme create(ThemeRequestDto themeRequest) {
        Name name = new Name(themeRequest.name());
        if (themeDao.existsByName(name)) {
            throw new ConflictException("이미 존재하는 테마 이름입니다.");
        }

        Theme theme = new Theme(name, themeRequest.thumbnailUrl(), themeRequest.description());
        return themeDao.insert(theme);
    }

    @Transactional
    public void delete(Long id) {
        Theme theme = themeDao.findById(id)
                .orElseThrow(() -> new NotFoundException("존재하지 않는 테마입니다."));
        if (reservationDao.existsByThemeId(theme.getId())) {
            throw new ConflictException("예약이 존재하여 테마를 삭제할 수 없습니다.");
        }

        themeDao.delete(theme.getId());
    }

    public List<AvailableTimeResponseDto> findAvailableTimesById(Long themeId, LocalDate localDate) {
        return themeDao.findAvailableTimesById(themeId, localDate);
    }

    public List<Theme> findPopulars(PopularThemeRequestDto popularThemeRequestDto) {
        return themeDao.findPopulars(popularThemeRequestDto);
    }
}
