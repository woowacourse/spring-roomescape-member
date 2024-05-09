package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.dto.request.AdminReservationRequest;
import roomescape.dto.request.MemberRequest;
import roomescape.dto.request.UserReservationRequest;
import roomescape.dto.response.ReservationResponse;
import roomescape.dto.response.SelectableTimeResponse;

import java.time.LocalDate;
import java.util.List;

@Service
public interface ReservationService {
    ReservationResponse save(final UserReservationRequest userReservationRequest, final MemberRequest memberRequest);

    ReservationResponse save(AdminReservationRequest reservationRequest);

    List<ReservationResponse> findAll();

    void delete(final long id);

    List<SelectableTimeResponse> findSelectableTime(final LocalDate date, final long themeId);
}
