package roomescape.application;

import java.time.Clock;
import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.application.dto.ThemeRequest;
import roomescape.application.dto.ThemeResponse;
import roomescape.exception.RoomescapeErrorCode;
import roomescape.exception.RoomescapeException;
import roomescape.domain.ReservationQueryRepository;
import roomescape.domain.Theme;
import roomescape.domain.ThemeRepository;

@Service
public class ThemeService {
    private final ThemeRepository themeRepository;
    private final ReservationQueryRepository reservationQueryRepository;
    private final Clock clock;

    public ThemeService(ThemeRepository themeRepository, ReservationQueryRepository reservationQueryRepository, Clock clock) {
        this.themeRepository = themeRepository;
        this.reservationQueryRepository = reservationQueryRepository;
        this.clock = clock;
    }

    public ThemeResponse create(ThemeRequest request) {
        Theme savedTheme = themeRepository.create(request.toTheme());
        return ThemeResponse.from(savedTheme);
    }

    public List<ThemeResponse> findAll() {
        return themeRepository.findAll()
                .stream()
                .map(ThemeResponse::from)
                .toList();
    }

    @Transactional
    public void deleteById(long id) {
        Theme theme = themeRepository.findById(id)
                .orElseThrow(() -> new RoomescapeException(RoomescapeErrorCode.NOT_FOUND_THEME));
        themeRepository.deleteById(theme.getId());
    }


    public List<ThemeResponse> findPopularThemes() {
        LocalDate today = LocalDate.now(clock);
        return reservationQueryRepository.findPopularThemesDateBetween(
                        today.minusDays(8),
                        today.minusDays(1),
                        10)
                .stream()
                .map(ThemeResponse::from)
                .toList();
    }
}
