package roomescape.service;

import static roomescape.exception.ExceptionType.NOT_FOUND_THEME;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.stereotype.Service;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.dto.AvailableTimeResponse;
import roomescape.exception.RoomescapeException;
import roomescape.repository.ReservationTimeRepository;
import roomescape.repository.ThemeRepository;

@Service
public class AvailableTimeService {
    private final ReservationTimeRepository reservationTimeRepository;
    private final ThemeRepository themeRepository;

    public AvailableTimeService(ReservationTimeRepository reservationTimeRepository, ThemeRepository themeRepository) {
        this.reservationTimeRepository = reservationTimeRepository;
        this.themeRepository = themeRepository;
    }

    public List<AvailableTimeResponse> findByThemeAndDate(LocalDate date, long themeId) {
        Theme theme = themeRepository.findById(themeId)
                .orElseThrow(() -> new RoomescapeException(NOT_FOUND_THEME));

        var alreadyUsedTimes = new HashSet<>(reservationTimeRepository.findUsedTimeByDateAndTheme(date, theme));

        return reservationTimeRepository.findAll().stream()
                .map(reservationTime -> toResponse(alreadyUsedTimes, reservationTime))
                .toList();
    }

    private AvailableTimeResponse toResponse(Set<ReservationTime> alreadyUsedTimes, ReservationTime reservationTime) {
        boolean isBooked = alreadyUsedTimes.contains(reservationTime);
        long id = reservationTime.getId();
        return new AvailableTimeResponse(id, reservationTime.getStartAt(), isBooked);
    }
}
