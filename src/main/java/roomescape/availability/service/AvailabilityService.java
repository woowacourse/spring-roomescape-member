package roomescape.availability.service;

import java.time.LocalDate;
import java.util.List;

import roomescape.time.domain.ReservationTime;

public interface AvailabilityService {

    List<ReservationTime> getAvailableTimes(Long themeId, LocalDate date);
}
