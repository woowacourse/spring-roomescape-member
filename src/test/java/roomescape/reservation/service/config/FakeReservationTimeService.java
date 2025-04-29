package roomescape.reservation.service.config;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import roomescape.time.controller.request.ReservationTimeCreateRequest;
import roomescape.time.controller.response.ReservationTimeResponse;
import roomescape.time.domain.ReservationTime;
import roomescape.time.service.ReservationTimeService;

public class FakeReservationTimeService implements ReservationTimeService {

    @Override
    public ReservationTimeResponse create(ReservationTimeCreateRequest request) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        return new ReservationTimeResponse(1L, request.startAt().format(formatter));
    }

    @Override
    public List<ReservationTimeResponse> getAll() {
        return List.of(
                new ReservationTimeResponse(1L, "10:00"),
                new ReservationTimeResponse(2L, "11:00")
        );
    }

    @Override
    public ReservationTime getReservationTime(Long id) {
        return new ReservationTime(id, LocalTime.of(10, 0));
    }

    @Override
    public void deleteById(Long id) {
    }
}
