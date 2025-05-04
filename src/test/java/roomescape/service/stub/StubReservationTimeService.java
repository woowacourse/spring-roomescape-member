package roomescape.service.stub;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import roomescape.domain.ReservationTime;
import roomescape.dto.request.ReservationTimeCreateRequest;
import roomescape.dto.response.ReservationTimeResponse;
import roomescape.dto.response.search.ReservationTimeResponseWithBookedStatus;
import roomescape.service.ReservationTimeService;

public class StubReservationTimeService extends ReservationTimeService {
    private final ReservationTime testReservationTime = new ReservationTime(1L, LocalTime.of(12,0));

    public StubReservationTimeService() {
        super(null, null);
    }

    @Override
    public ReservationTimeResponse createReservationTime(ReservationTimeCreateRequest request) {
        return ReservationTimeResponse.from(testReservationTime);
    }

    @Override
    public List<ReservationTimeResponse> findAll() {
        return List.of(ReservationTimeResponse.from(testReservationTime));
    }

    @Override
    public void deleteReservationTimeById(Long id) {
    }

    @Override
    public List<ReservationTimeResponseWithBookedStatus> findAvailableReservationTimesByDateAndThemeId(LocalDate date, Long themeId) {
        return List.of(ReservationTimeResponseWithBookedStatus.of(testReservationTime, false));
    }
}
