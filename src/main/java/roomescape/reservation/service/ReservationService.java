package roomescape.reservation.service;

import java.time.LocalDate;
import java.util.List;

import roomescape.reservation.domain.Reservation;
import roomescape.reservation.service.dto.ReservationSaveServiceDto;

public interface ReservationService {
    List<Reservation> getAll();
    Reservation create(ReservationSaveServiceDto reservation);
    void cancel(Long id);

    List<Reservation> getByName(String name);
    void cancelForUser(Long id);
    Reservation update(Long id, LocalDate date, Long timeId);
}
