package roomescape.reservation.repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.domain.ReservationStatus;

public interface ReservationRepository {
    List<Reservation> findAll();

    List<Reservation> findAllByNameOrderByDateAndTime(String name);

    Optional<Reservation> findById(Long id);

    Reservation save(Reservation reservation);

    boolean existsByDateAndTimeAndThemeId(LocalDate date, LocalTime time, long themeId, ReservationStatus status);

    boolean existsByDateAndTimeAndThemeId(LocalDate date, LocalTime time, long themeId, long excludeId, ReservationStatus status);

    boolean existsByNameAndDateAndTime(String name, LocalDate date, LocalTime time);

    boolean existsByTimeId(long timeId, ReservationStatus status);

    Reservation updateStatus(Reservation reservation);

    Reservation updateDateAndTime(Reservation reservation);
}
