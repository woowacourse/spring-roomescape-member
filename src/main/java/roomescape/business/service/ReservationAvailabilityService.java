package roomescape.business.service;

import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.business.domain.ReservationAvailability;
import roomescape.business.domain.Theme;
import roomescape.repository.ReservationAvailabilityRepository;
import roomescape.repository.ThemeRepository;
import roomescape.presentation.dto.TimeAvailabilityResponse;

@Service
public class ReservationAvailabilityService {

    private final ReservationAvailabilityRepository reservationAvailabilityRepository;
    private final ThemeRepository themeRepository;

    public ReservationAvailabilityService(final ReservationAvailabilityRepository reservationAvailabilityRepository,
                                          final ThemeRepository themeRepository) {
        this.reservationAvailabilityRepository = reservationAvailabilityRepository;
        this.themeRepository = themeRepository;
    }

    public List<TimeAvailabilityResponse> getAllTimeAvailability(final LocalDate date, final long themeId) {
        final Theme theme = themeRepository.findById(themeId)
                .orElseThrow(() -> new IllegalArgumentException("테마가 존재하지 않습니다."));
        final List<ReservationAvailability> reservationAvailabilities = reservationAvailabilityRepository.
                findAllByDateAndTheme(date, theme);
        return reservationAvailabilities.stream()
                .map(reservationAvailability -> new TimeAvailabilityResponse(
                        reservationAvailability.getTimeId(),
                        reservationAvailability.getStartAt(),
                        reservationAvailability.isBooked()
                )).toList();
    }
}
