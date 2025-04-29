package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.dto.ThemeRequestDto;
import roomescape.dto.ThemeResponseDto;
import roomescape.entity.Theme;
import roomescape.exception.EntityNotFoundException;
import roomescape.exception.ReservationExistException;
import roomescape.repository.ReservationDao;
import roomescape.repository.ThemeDao;

import java.util.List;

@Service
public class ThemeService {

    private final ReservationDao reservationDao;
    private final ThemeDao themeDao;

    public ThemeService(ReservationDao reservationDao, ThemeDao themeDao) {
        this.reservationDao = reservationDao;
        this.themeDao = themeDao;
    }

    public List<ThemeResponseDto> findAll() {
        List<Theme> themes = themeDao.findAll();
        return themes.stream()
            .map(this::createResponseDto)
            .toList();
    }

    private ThemeResponseDto createResponseDto(Theme theme) {
        return new ThemeResponseDto(theme.getId(), theme.getName(), theme.getDescription(), theme.getThumbnail());
    }

    public ThemeResponseDto add(ThemeRequestDto requestDto) {
        Theme theme = new Theme(requestDto.name(), requestDto.description(), requestDto.thumbnail());
        Theme savedTheme = themeDao.save(theme);
        return createResponseDto(savedTheme);
    }

    public void deleteById(Long id) {
        if (reservationDao.findByTimeId(id).isPresent()) {
            throw new ReservationExistException("이 시간의 예약이 존재합니다.");
        }

        themeDao.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("삭제할 예약시간이 없습니다."));

        themeDao.deleteById(id);
    }
}
