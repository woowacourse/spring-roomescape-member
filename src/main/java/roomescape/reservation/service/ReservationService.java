package roomescape.reservation.service;

import java.util.List;
import roomescape.reservation.dto.AdminReservationRequest;
import roomescape.reservation.dto.ReservationResponse;
import roomescape.reservation.dto.UserReservationRequest;

public interface ReservationService {
    ReservationResponse createForUser(UserReservationRequest request, Long memberId);

    List<ReservationResponse> getAll();

    void deleteById(Long id);

    ReservationResponse createForAdmin(AdminReservationRequest request);
}
