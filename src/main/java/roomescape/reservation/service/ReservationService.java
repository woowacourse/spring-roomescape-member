package roomescape.reservation.service;

import roomescape.reservation.controller.dto.AvailableReservationTimeWebResponse;
import roomescape.reservation.controller.dto.CreateReservationWebRequest;
import roomescape.reservation.controller.dto.ReservationResponse;

import java.time.LocalDate;
import java.util.List;

public interface ReservationService {

    List<ReservationResponse> getAll();

    List<AvailableReservationTimeWebResponse> getAvailable(LocalDate date, Long id);

    ReservationResponse create(CreateReservationWebRequest createReservationWebRequest);

    void delete(Long id);
}
