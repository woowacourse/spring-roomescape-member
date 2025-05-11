package roomescape.reservation.service;

import java.time.LocalDate;
import java.util.List;
import roomescape.reservation.dto.AdminReservationRequest;
import roomescape.reservation.dto.ReservationResponse;
import roomescape.reservation.dto.UserReservationRequest;

public interface ReservationService {
    ReservationResponse createForUser(UserReservationRequest request, Long memberId);

    List<ReservationResponse> getFiltered(Long memberId, Long themeId, LocalDate from, LocalDate to);

    void deleteById(Long id);

    ReservationResponse createForAdmin(AdminReservationRequest request);
}
