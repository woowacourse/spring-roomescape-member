package roomescape.theme.business.service;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.global.exception.impl.BadRequestException;
import roomescape.global.exception.impl.NotFoundException;
import roomescape.reservation.business.repository.ReservationDao;
import roomescape.theme.business.domain.Theme;
import roomescape.theme.business.repository.ThemeDao;
import roomescape.theme.presentation.request.ThemeRequest;
import roomescape.theme.presentation.response.ThemeResponse;

@Service
public class ThemeService {

    private final ReservationDao reservationDao;
    private final ThemeDao themeDao;

    public ThemeService(ReservationDao reservationDao, ThemeDao themeDao) {
        this.reservationDao = reservationDao;
        this.themeDao = themeDao;
    }

    public List<ThemeResponse> findAll() {
        List<Theme> themes = themeDao.findAll();
        return themes.stream()
                .map(ThemeResponse::of)
                .toList();
    }

    public ThemeResponse add(ThemeRequest requestDto) {
        if (themeDao.existByName(requestDto.name())) {
            throw new BadRequestException("동일한 이름의 테마가 이미 존재합니다.");
        }
        Theme theme = new Theme(requestDto.name(), requestDto.description(), requestDto.thumbnail());
        Theme savedTheme = themeDao.save(theme);
        return ThemeResponse.of(savedTheme);
    }

    public void deleteById(Long id) {
        if (reservationDao.existByTimeId(id)) {
            throw new BadRequestException("이 테마의 예약이 존재합니다.");
        }
        int affectedRows = themeDao.deleteById(id);
        if (affectedRows == 0) {
            throw new NotFoundException("삭제할 테마가 없습니다.");
        }
    }

    public List<ThemeResponse> sortByRank() {
        List<Theme> themes = themeDao.sortByRank();
        return themes.stream()
                .map(ThemeResponse::of)
                .toList();
    }
}
