package roomescape.business.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import roomescape.business.model.entity.Theme;
import roomescape.business.model.repository.ReservationRepository;
import roomescape.business.model.repository.ThemeRepository;
import roomescape.exception.business.NotFoundException;
import roomescape.exception.business.RelatedEntityExistException;

import java.time.LocalDate;
import java.util.List;

import static roomescape.exception.ErrorCode.RESERVED_THEME;
import static roomescape.exception.ErrorCode.THEME_NOT_EXIST;

@Service
@RequiredArgsConstructor
public class ThemeService {

    private static final int AGGREGATE_START_DATE_INTERVAL = 7;
    private static final int AGGREGATE_END_DATE_INTERVAL = 1;

    private final ThemeRepository themeRepository;
    private final ReservationRepository reservationRepository;

    public Theme addAndGet(final String name, final String description, final String thumbnail) {
        Theme theme = Theme.create(name, description, thumbnail);
        themeRepository.save(theme);
        return theme;
    }

    public List<Theme> getAll() {
        return themeRepository.findAll();
    }

    public List<Theme> getPopular(int size) {
        LocalDate now = LocalDate.now();
        return themeRepository.findPopularThemes(
                now.minusDays(AGGREGATE_START_DATE_INTERVAL),
                now.minusDays(AGGREGATE_END_DATE_INTERVAL),
                size
        );
    }

    public void delete(final String id) {
        if (reservationRepository.existByThemeId(id)) {
            throw new RelatedEntityExistException(RESERVED_THEME);
        }
        if (!themeRepository.existById(id)) {
            throw new NotFoundException(THEME_NOT_EXIST);
        }
        themeRepository.deleteById(id);
    }
}
