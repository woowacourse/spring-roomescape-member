package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.dto.request.MemberRequest;
import roomescape.dto.request.ReservationRequest;
import roomescape.dto.response.ReservationResponse;
import roomescape.dto.response.SelectableTimeResponse;

import java.time.LocalDate;
import java.util.List;

@Service
public interface ReservationService {
    ReservationResponse save(final ReservationRequest reservationRequest, final MemberRequest memberRequest);

    List<ReservationResponse> findAll();

    void delete(final long id);

    List<SelectableTimeResponse> findSelectableTime(final LocalDate date, final long themeId);
}
