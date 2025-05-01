package roomescape.business.service;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.business.model.entity.Theme;
import roomescape.business.model.repository.ReservationDao;
import roomescape.business.model.repository.ThemeDao;
import roomescape.presentation.dto.request.ThemeRequest;
import roomescape.presentation.dto.response.ThemeResponse;
import roomescape.exception.EntityNotFoundException;
import roomescape.exception.ReservationExistException;
import roomescape.exception.ThemeExistException;

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
            throw new ThemeExistException("동일한 이름의 테마가 이미 존재합니다.");
        }
        Theme theme = new Theme(requestDto.name(), requestDto.description(), requestDto.thumbnail());
        Theme savedTheme = themeDao.save(theme);
        return ThemeResponse.of(savedTheme);
    }

    public void deleteById(Long id) {
        if (reservationDao.existByTimeId(id)) {
            throw new ReservationExistException("이 시간의 예약이 존재합니다.");
        }
        themeDao.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("삭제할 예약시간이 없습니다."));
        themeDao.deleteById(id);
    }

    public List<ThemeResponse> sortByRank() {
        List<Theme> themes = themeDao.sortByRank();
        return themes.stream()
                .map(ThemeResponse::of)
                .toList();
    }
}
