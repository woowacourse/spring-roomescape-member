package roomescape.reservation.domain.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import roomescape.reservation.domain.Reservation;

public interface ReservationRepository {
    Reservation save(Reservation reservation);

    List<Reservation> findAll();

    boolean deleteById(long reservationId);

    boolean existByTimeId(long timeId);

    boolean existBy(LocalDate date, long timeId, long themeId);

    void saveReservationList(long memberId, long reservationId);
}
