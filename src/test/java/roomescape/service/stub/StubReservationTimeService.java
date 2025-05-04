package roomescape.service.stub;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import roomescape.domain.ReservationTime;
import roomescape.dto.request.ReservationTimeCreateRequest;
import roomescape.dto.response.search.ReservationTimeResponseWithBookedStatus;
import roomescape.service.ReservationTimeService;

public class StubReservationTimeService extends ReservationTimeService {
    private final ReservationTime testReservationTime = new ReservationTime(1L, LocalTime.of(12,0));

    public StubReservationTimeService() {
        super(null, null);
    }

    @Override
    public ReservationTime createReservationTime(ReservationTimeCreateRequest request) {
        return testReservationTime;
    }

    @Override
    public List<ReservationTime> findAll() {
        return List.of(testReservationTime);
    }

    @Override
    public void deleteReservationTimeById(Long id) {
    }

    @Override
    public List<ReservationTimeResponseWithBookedStatus> findAvailableReservationTimesByDateAndThemeId(LocalDate date, Long themeId) {
        return List.of(ReservationTimeResponseWithBookedStatus.of(testReservationTime, false));
    }
}
