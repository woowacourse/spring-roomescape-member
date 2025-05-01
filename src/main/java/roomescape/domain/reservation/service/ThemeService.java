package roomescape.domain.reservation.service;

import java.time.Clock;
import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.common.exception.AlreadyInUseException;
import roomescape.domain.reservation.dto.ThemeRequest;
import roomescape.domain.reservation.dto.ThemeResponse;
import roomescape.domain.reservation.entity.Theme;
import roomescape.domain.reservation.repository.ReservationRepository;
import roomescape.domain.reservation.repository.ThemeRepository;

@Service
public class ThemeService {

    private static final int START_DATE_OFFSET = 8;
    private static final int END_DATE_OFFSET = 1;

    private final Clock clock;
    private final ThemeRepository themeRepository;
    private final ReservationRepository reservationRepository;

    public ThemeService(Clock clock, ThemeRepository themeRepository, ReservationRepository reservationRepository) {
        this.clock = clock;
        this.themeRepository = themeRepository;
        this.reservationRepository = reservationRepository;
    }

    public List<ThemeResponse> getAll() {
        return themeRepository.findAll()
                .stream()
                .map(ThemeResponse::from)
                .toList();
    }

    public ThemeResponse create(ThemeRequest request) {
        Theme theme = themeRepository.save(Theme.withoutId(request.name(), request.description(), request.thumbnail()));
        return ThemeResponse.from(theme);
    }

    public void delete(Long id) {
        if (reservationRepository.existsByThemeId(id)) {
            throw new AlreadyInUseException("Theme with id " + id + " is currently in use by reservations");
        }

        themeRepository.deleteById(id);
    }

    public List<ThemeResponse> getPopularThemes() {
        LocalDate now = getNow();

        LocalDate startDate = now.minusDays(START_DATE_OFFSET);
        LocalDate endDate = now.minusDays(END_DATE_OFFSET);

        return themeRepository.findThemeRankingByReservation(startDate, endDate)
                .stream()
                .map(ThemeResponse::from)
                .toList();
    }

    private LocalDate getNow() {
        return LocalDate.now(clock);
    }
}
