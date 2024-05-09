package roomescape.repository.reservation;

import java.time.LocalDate;
import java.util.List;
import roomescape.domain.Reservation;

public interface ReservationRepository {

    Reservation save(Reservation reservation);

    void delete(Long id);

    List<Reservation> findAll();

    List<Long> findAlreadyBookedTimeIds(LocalDate date, Long themeId);

    boolean isAlreadyBooked(Reservation reservation);
}
