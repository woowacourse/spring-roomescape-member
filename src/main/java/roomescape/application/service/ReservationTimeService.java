package roomescape.application.service;

import java.time.LocalDate;
import java.util.List;
import roomescape.application.dto.ReservationTimeAvailableResponse;
import roomescape.application.dto.ReservationTimeRequest;
import roomescape.application.dto.ReservationTimeResponse;

public interface ReservationTimeService {

    List<ReservationTimeResponse> findAllReservationTimes();

    List<ReservationTimeAvailableResponse> findAvailableTimes(LocalDate date, Long themeId);

    ReservationTimeResponse createReservationTime(ReservationTimeRequest request);

    void deleteReservationTime(long id);
}
