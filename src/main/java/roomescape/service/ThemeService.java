package roomescape.service;

import java.util.List;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.domain.theme.PopularThemeCondition;
import roomescape.domain.theme.Theme;
import roomescape.domain.theme.ReservationThemeCommand;
import roomescape.domain.theme.ReservationThemeWithCount;
import roomescape.exception.ConflictException;
import roomescape.exception.NotFoundResourceException;
import roomescape.repository.theme.ThemeRepository;
import roomescape.repository.reservation.ReservationRepository;

@Service
public class ThemeService {
    private static final String INVALID_THEME_ID = "존재하지 않은 테마 id입니다.";
    private static final String CANNOT_DELETE_THEME_IN_USE = "해당 테마를 참조하는 예약 데이터가 존재하기 때문에 삭제할 수 없습니다.";
    private static final String INTEGRITY_VIOLATION_ON_DELETE = "데이터 무결성 위반으로 삭제에 실패했습니다.";

    private final ThemeRepository themeRepository;
    private final ReservationRepository reservationRepository;

    public ThemeService(ThemeRepository themeRepository, ReservationRepository reservationRepository) {
        this.themeRepository = themeRepository;
        this.reservationRepository = reservationRepository;
    }

    @Transactional
    public Theme addTheme(ReservationThemeCommand reservationThemeCommand) {
        return themeRepository.addTheme(reservationThemeCommand);
    }

    public List<Theme> getAllTheme() {
        return themeRepository.getAllTheme();
    }

    public void deleteTheme(long id) {
        boolean isExistThemeId = themeRepository.isExistsById(id);

        if(!isExistThemeId) {
            throw new NotFoundResourceException(INVALID_THEME_ID);
        }

        boolean hasTheme = reservationRepository.existsByThemeId(id);

        if(hasTheme) {
            throw new ConflictException(CANNOT_DELETE_THEME_IN_USE);
        }

        try {
            themeRepository.deleteTheme(id);
        } catch (DataIntegrityViolationException e) {
            throw new ConflictException(INTEGRITY_VIOLATION_ON_DELETE);
        }
    }

    public List<ReservationThemeWithCount> getPopularTheme(PopularThemeCondition popularThemeCondition) {
        return themeRepository.getPopularTheme(popularThemeCondition);
    }
}
