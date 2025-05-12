package roomescape.reservation.application;

import roomescape.auth.session.Session;
import roomescape.reservation.ui.dto.AvailableReservationTimeWebResponse;
import roomescape.reservation.ui.dto.CreateReservationWebRequest;
import roomescape.reservation.ui.dto.ReservationResponse;

import java.time.LocalDate;
import java.util.List;

public interface ReservationFacade {

    List<ReservationResponse> getAll();

    List<AvailableReservationTimeWebResponse> getAvailable(LocalDate date, Long themeId);

    ReservationResponse create(CreateReservationWebRequest request, Session session);

    void delete(Long id);
}
