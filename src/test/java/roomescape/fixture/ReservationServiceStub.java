package roomescape.fixture;

import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.dto.ReservationRequest;
import roomescape.dto.ReservationResponse;
import roomescape.service.ReservationService;

public class ReservationServiceStub extends ReservationService {
    private final Map<Long, Reservation> reservations = new HashMap<>();
    private long id = 1L;

    public ReservationServiceStub() {
        super(null, null);
    }

    @Override
    public ReservationResponse create(ReservationRequest request) {
        Reservation reservation = new Reservation(request.getName(), request.getDate(),
                new ReservationTime(request.getTimeId(), LocalTime.now()));
        Reservation saved = reservation.withId(id++);
        reservations.put(reservation.getId(), saved);
        return ReservationResponse.from(saved);
    }

    @Override
    public List<ReservationResponse> getAll() {
        return ReservationResponse.from(reservations.values().stream().toList());
    }

    @Override
    public void deleteById(Long id) {
        reservations.remove(id);
    }
}
