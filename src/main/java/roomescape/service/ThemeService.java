package roomescape.service;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.domain.theme.Theme;
import roomescape.global.exception.RoomescapeException;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ThemeRepository;
import roomescape.service.dto.SaveThemeDto;

@Service
public class ThemeService {

    private static final int POPULAR_START_DATE = 8;
    private static final int POPULAR_END_DATE = 1;
    private static final int POPULAR_THEME_COUNT = 10;

    private final ThemeRepository themeRepository;
    private final ReservationRepository reservationRepository;

    public ThemeService(ThemeRepository themeRepository, ReservationRepository reservationRepository) {
        this.themeRepository = themeRepository;
        this.reservationRepository = reservationRepository;
    }

    public Theme save(SaveThemeDto request) {
        Theme theme = new Theme(request.name(), request.description(), request.thumbnail());
        validateDuplication(request);
        return themeRepository.save(theme);
    }

    private void validateDuplication(SaveThemeDto request) {
        if (themeRepository.isExists(request.name())) {
            throw new RoomescapeException("같은 이름의 테마가 이미 존재합니다.");
        }
    }

    public int delete(Long id) {
        if (reservationRepository.isThemeIdUsed(id)) {
            throw new RoomescapeException("해당 테마를 사용하는 예약이 존재하여 삭제할 수 없습니다.");
        }
        return themeRepository.deleteById(id);
    }

    public List<Theme> findAll() {
        return themeRepository.findAll();
    }

    public List<Theme> findPopular() {
        return themeRepository.findPopular(POPULAR_START_DATE, POPULAR_END_DATE, POPULAR_THEME_COUNT);
    }
}
