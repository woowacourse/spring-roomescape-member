package roomescape.reservation.service;

import java.util.List;

import roomescape.reservation.domain.Reservation;
import roomescape.reservation.service.dto.ReservationSaveServiceDto;
import roomescape.reservation.service.dto.ReservationUpdateServiceDto;

public interface ReservationService {
    List<Reservation> getAll();
    List<Reservation> findByName(String name);
    Reservation create(ReservationSaveServiceDto reservation);
    Reservation update(ReservationUpdateServiceDto dto);
    void cancel(Long id);
    void cancel(Long id, String requesterName);
}
