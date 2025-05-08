package roomescape.reservation.application;

import roomescape.reservation.ui.dto.AvailableReservationTimeWebResponse;
import roomescape.reservation.ui.dto.CreateReservationWebRequest;
import roomescape.reservation.ui.dto.ReservationResponse;

import java.time.LocalDate;
import java.util.List;

public interface ReservationWebFacade {

    List<ReservationResponse> getAll();

    List<AvailableReservationTimeWebResponse> getAvailable(LocalDate date, Long themeId);

    ReservationResponse create(CreateReservationWebRequest createReservationWebRequest);

    void delete(Long id);
}
