package roomescape.domain.reservation.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import roomescape.domain.reservation.entity.Reservation;

public interface ReservationRepository {

    List<Reservation> findAllReservations();

    Optional<Reservation> findReservationByDateTimeAndThemeId(LocalDate date, Long timeId, Long themeId);

    List<Long> findTimeIdsByDateAndThemeId(LocalDate localDate, Long themeId);

    Reservation save(Reservation reservation);

    boolean existsByTimeId(Long timeId);

    int deleteReservationById(Long id);
}
