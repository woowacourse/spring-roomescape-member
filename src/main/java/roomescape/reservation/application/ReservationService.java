package roomescape.reservation.application;

import roomescape.reservation.domain.ReservationId;
import roomescape.reservation.ui.dto.CreateReservationWebRequest;
import roomescape.reservation.ui.dto.ReservationResponse;
import roomescape.theme.domain.ThemeId;

import java.time.LocalDate;
import java.util.List;

public interface ReservationService {

    List<ReservationResponse> getAll();

    List<AvailableReservationTimeWebResponse> getAvailable(LocalDate date, ThemeId themeId);

    ReservationResponse create(CreateReservationWebRequest createReservationWebRequest);

    void delete(ReservationId id);
}
