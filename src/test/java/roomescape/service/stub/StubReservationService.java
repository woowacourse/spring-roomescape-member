package roomescape.service.stub;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import roomescape.domain.*;
import roomescape.dto.request.ReservationCreateRequest;
import roomescape.dto.response.ReservationResponse;
import roomescape.service.ReservationService;

public class StubReservationService extends ReservationService {
    private final Reservation testReservation = new Reservation(
            1L,
            LocalDate.of(2025, 5, 1),
            new ReservationTime(1L, LocalTime.of(12, 0)),
            new Theme(1L, "name", "description", "thumbnail"),
            new Member(1L,"히스타","test@test.com", MemberRole.USER,"1234")
    );

    public StubReservationService() {
        super(null, null, null, null);
    }

    @Override
    public void deleteReservationById(Long id) {
    }

    @Override
    public List<ReservationResponse> findAll() {
        return List.of(ReservationResponse.from(testReservation));
    }

    @Override
    public ReservationResponse createReservation(ReservationCreateRequest reservationCreateRequest, Long memberId) {
        return ReservationResponse.from(testReservation);
    }
}
