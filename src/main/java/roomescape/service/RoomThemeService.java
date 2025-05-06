package roomescape.service;

import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.domain.RoomTheme;
import roomescape.exception.custom.BusinessRuleViolationException;
import roomescape.exception.custom.ExistedDuplicateValueException;
import roomescape.exception.custom.NotFoundValueException;
import roomescape.repository.ReservationRepository;
import roomescape.repository.RoomThemeRepository;
import roomescape.service.dto.request.RoomThemeCreation;
import roomescape.service.dto.response.PopularThemeResult;
import roomescape.service.dto.response.RoomThemeResult;

@Service
public class RoomThemeService {

    public static final int TOP_LIMIT = 10;

    private final ReservationRepository reservationRepository;
    private final RoomThemeRepository roomThemeRepository;

    public RoomThemeService(final ReservationRepository reservationRepository,
                            final RoomThemeRepository roomThemeRepository) {
        this.reservationRepository = reservationRepository;
        this.roomThemeRepository = roomThemeRepository;
    }

    public RoomThemeResult addTheme(final RoomThemeCreation themeCreation) {
        if (roomThemeRepository.existsByName(themeCreation.name())) {
            throw new ExistedDuplicateValueException("이미 존재하는 테마입니다");
        }

        final RoomTheme theme = new RoomTheme(themeCreation.name(), themeCreation.description(),
                themeCreation.thumbnail());
        final long id = roomThemeRepository.insert(theme);

        final RoomTheme savedTheme = findById(id);
        return RoomThemeResult.from(savedTheme);
    }

    private RoomTheme findById(final long id) {
        return roomThemeRepository.findById(id)
                .orElseThrow(() -> new NotFoundValueException("존재하지 않는 테마입니다"));
    }

    public List<RoomThemeResult> findAllThemes() {
        return roomThemeRepository.findAll()
                .stream()
                .map(RoomThemeResult::from)
                .toList();
    }

    public List<PopularThemeResult> findPopularThemes() {
        final LocalDate currentDate = LocalDate.now();
        final LocalDate start = currentDate.minusDays(8);
        final LocalDate end = currentDate.minusDays(1);

        return roomThemeRepository.findPopularThemes(start, end, TOP_LIMIT).stream()
                .map(PopularThemeResult::from)
                .toList();
    }

    public void deleteTheme(final long id) {
        if (reservationRepository.existsByThemeId(id)) {
            throw new BusinessRuleViolationException("사용 중인 테마입니다");
        }

        final boolean deleted = roomThemeRepository.deleteById(id);

        if (!deleted) {
            throw new NotFoundValueException("존재하지 않는 테마입니다");
        }
    }
}
