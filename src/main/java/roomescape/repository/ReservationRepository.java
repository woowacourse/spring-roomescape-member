package roomescape.repository;

import roomescape.domain.Reservation;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ReservationRepository {
    List<Reservation> findAll();

    Optional<Reservation> findById(Long reservationId);

    Reservation save(Reservation reservation);

    void deleteById(Long reservationId);

    boolean existByDateAndTimeIdAndThemeId(LocalDate date, Long timeId, Long themeId);

    boolean existByTimeId(Long reservationTimeId);

    List<Reservation> findAllByDateAndThemeId(LocalDate date, Long themeId);
}
