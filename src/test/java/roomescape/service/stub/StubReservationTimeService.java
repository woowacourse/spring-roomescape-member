package roomescape.service.stub;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.dto.request.ReservationTimeRequest;
import roomescape.dto.response.search.ReservationTimeResponseWithBookedStatus;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationTimeRepository;
import roomescape.service.ReservationTimeService;
import roomescape.service.ThemeService;

public class StubReservationTimeService extends ReservationTimeService {
    private final ReservationTime testReservationTime = new ReservationTime(1L, LocalTime.of(12,0));

    public StubReservationTimeService() {
        super(null, null);
    }

    @Override
    public ReservationTime saveReservationTime(ReservationTimeRequest request) {
        return testReservationTime;
    }

    @Override
    public List<ReservationTime> readReservationTime() {
        return List.of(testReservationTime);
    }

    @Override
    public void deleteReservationTime(Long id) {
    }

    @Override
    public List<ReservationTimeResponseWithBookedStatus> readAvailableTimesBy(LocalDate date, Long themeId) {
        return List.of(ReservationTimeResponseWithBookedStatus.of(testReservationTime, false));
    }
}
