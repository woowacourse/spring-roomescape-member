package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.dto.request.ThemeRequest;
import roomescape.dto.response.ThemeResponse;
import roomescape.entity.Theme;
import roomescape.exception.EntityNotFoundException;
import roomescape.exception.ReservationExistException;
import roomescape.exception.ThemeExistException;
import roomescape.repository.ReservationDao;
import roomescape.repository.ThemeDao;

import java.time.LocalDate;
import java.util.List;

@Service
public class ThemeService {

    private static final int END_OFFSET_DAYS = 1;
    private static final int LOOK_BACK_DAYS = 6;

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
        if (themeDao.isExistByName(requestDto.name())) {
            throw new ThemeExistException("동일한 이름의 테마가 이미 존재합니다.");
        }
        Theme theme = new Theme(requestDto.name(), requestDto.description(), requestDto.thumbnail());
        Theme savedTheme = themeDao.save(theme);
        return ThemeResponse.of(savedTheme);
    }

    public void deleteById(Long id) {
        if (reservationDao.isExistByTimeId(id)) {
            throw new ReservationExistException("이 시간의 예약이 존재합니다.");
        }

        themeDao.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("삭제할 테마가 없습니다."));

        themeDao.deleteById(id);
    }

    public List<ThemeResponse> sortByRankForLastWeek() {
        LocalDate endDate = LocalDate.now().minusDays(END_OFFSET_DAYS);
        LocalDate startDate = endDate.minusDays(LOOK_BACK_DAYS);

        List<Theme> themes = themeDao.sortByRank(startDate, endDate);
        return themes.stream()
            .map(ThemeResponse::of)
            .toList();
    }
}
