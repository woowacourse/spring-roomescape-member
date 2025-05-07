package roomescape.reservationtime.service;

import java.time.LocalDate;
import java.util.List;
import roomescape.reservationtime.dto.AvailableTimeResponse;
import roomescape.reservationtime.dto.ReservationTimeRequest;
import roomescape.reservationtime.dto.ReservationTimeResponse;

public interface ReservationTimeService {

    ReservationTimeResponse create(ReservationTimeRequest request);

    List<ReservationTimeResponse> getAll();

    void deleteById(Long id);

    List<AvailableTimeResponse> getAvailableTimes(LocalDate date, Long themeId);
}
