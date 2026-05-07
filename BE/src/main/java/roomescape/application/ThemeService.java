package roomescape.application;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.entity.ReservationRepository;
import roomescape.entity.Theme;
import roomescape.entity.ThemeRepository;
import roomescape.entity.ThemeSortType;
import roomescape.global.exception.ErrorCode;
import roomescape.global.exception.customException.ThemeException;

@Service
@Transactional(readOnly = true)
public class ThemeService {

    private final ThemeRepository themeRepository;
    private final ReservationRepository reservationRepository;

    public ThemeService(ThemeRepository themeRepository, ReservationRepository reservationRepository) {
        this.themeRepository = themeRepository;
        this.reservationRepository = reservationRepository;
    }

    @Transactional
    public Theme save(String name, String description, String thumbnailUrl) {
        return themeRepository.save(Theme.createWithNullId(name, description, thumbnailUrl));
    }

    public Optional<Theme> findById(Long id) {
        return themeRepository.findById(id);
    }

    public List<Theme> findAll() {
        return themeRepository.findAll();
    }

    public List<Theme> findTopNByPeriod(LocalDate startAt, LocalDate endAt, ThemeSortType sortType, Long limit) {
        return themeRepository.findTopNByPeriod(startAt, endAt, sortType, limit);
    }

    @Transactional
    public void deleteById(Long id) {
        if (reservationRepository.existsByThemeId(id)) {
            throw new ThemeException(ErrorCode.THEME_ALREADY_USED);
        }
        themeRepository.deleteById(id);
    }
}
