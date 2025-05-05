package roomescape.service;

import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.controller.dto.TimeAvailabilityResponse;
import roomescape.repository.ReservationAvailabilityDao;
import roomescape.repository.ThemeDao;
import roomescape.service.reservation.ReservationAvailability;
import roomescape.service.reservation.Theme;

@Service
public class ReservationAvailabilityService {

    private final ReservationAvailabilityDao reservationAvailabilityDao;
    private final ThemeDao themeDao;

    public ReservationAvailabilityService(final ReservationAvailabilityDao reservationAvailabilityDao,
                                          final ThemeDao themeDao) {
        this.reservationAvailabilityDao = reservationAvailabilityDao;
        this.themeDao = themeDao;
    }

    public List<TimeAvailabilityResponse> getAllTimeAvailability(final LocalDate date, final long themeId) {
        final Theme theme = themeDao.findById(themeId)
                .orElseThrow(() -> new IllegalArgumentException("테마가 존재하지 않습니다."));
        final List<ReservationAvailability> reservationAvailabilities = reservationAvailabilityDao.
                findAllByDateAndTheme(date, theme);
        return reservationAvailabilities.stream()
                .map(reservationAvailability -> new TimeAvailabilityResponse(
                        reservationAvailability.getTimeId(),
                        reservationAvailability.getStartAt(),
                        reservationAvailability.isBooked()
                )).toList();
    }
}
