package roomescape.application;

import java.time.Clock;
import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.application.dto.AvailableTimeResponse;
import roomescape.application.dto.ThemeResponse;
import roomescape.domain.ReservationQueryRepository;

@Service
public class ReservationQueryService {

    private final ReservationQueryRepository reservationQueryRepository;
    private final Clock clock;

    public ReservationQueryService(ReservationQueryRepository reservationQueryRepository, Clock clock) {
        this.reservationQueryRepository = reservationQueryRepository;
        this.clock = clock;
    }

    public List<AvailableTimeResponse> findAvailableTimes(LocalDate date, long themeId) {
        return reservationQueryRepository.findAvailableReservationTimes(date, themeId)
                .stream()
                .map(AvailableTimeResponse::from)
                .toList();
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
