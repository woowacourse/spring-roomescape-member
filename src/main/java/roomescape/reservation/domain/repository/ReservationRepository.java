package roomescape.reservation.domain.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import roomescape.reservation.domain.Reservation;

public interface ReservationRepository {
    Reservation save(Reservation reservation);

    List<Reservation> findAll();

    boolean deleteById(long reservationId);

    List<Reservation> findAllByTimeId(long timeId);

    boolean existsByDateTime(LocalDate date, long timeId);

    Reservation findBy(LocalDate date, long timeId, long themeId);

    void saveReservationList(long memberId, long reservationId);
}
