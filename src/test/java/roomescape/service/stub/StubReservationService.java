package roomescape.service.stub;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.dto.request.ReservationRequest;
import roomescape.dto.request.ReservationTimeRequest;
import roomescape.dto.response.ReservationResponse;
import roomescape.dto.response.search.ReservationTimeResponseWithBookedStatus;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationTimeRepository;
import roomescape.repository.ThemeRepository;
import roomescape.service.ReservationService;
import roomescape.service.ReservationTimeService;

public class StubReservationService extends ReservationService {
    private final Reservation testReservation = new Reservation(
            1L,
            "히스타",
            LocalDate.of(2025, 5, 1),
            new ReservationTime(1L, LocalTime.of(12, 0)),
            new Theme(1L, "name", "description", "thumbnail")
    );

    public StubReservationService() {
        super(null, null, null);
    }

    @Override
    public void delete(Long id) {
    }

    @Override
    public List<ReservationResponse> readReservation() {
        return List.of(ReservationResponse.from(testReservation));
    }

    @Override
    public ReservationResponse saveReservation(ReservationRequest request) {
        return ReservationResponse.from(testReservation);
    }
}
