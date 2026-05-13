package roomescape.domain.reservation.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import roomescape.domain.reservation.entity.Reservation;

public interface ReservationRepository {

    List<Reservation> findAllReservations();

    List<Reservation> findReservationsByName(String name);

    Optional<Reservation> findReservationById(Long id);

    List<Long> findTimeIdsByDateAndThemeId(LocalDate localDate, Long themeId);

    Reservation save(Reservation reservation);

    boolean existsByDateTimeAndThemeId(LocalDate date, Long timeId, Long themeId);

    boolean existsByDateTimeAndThemeIdExceptId(Long id, LocalDate date, Long timeId, Long themeId);

    boolean existsByTimeId(Long timeId);

    boolean existsByThemeId(Long themeId);

    void updateReservationById(Long id, LocalDate date, Long timeId);

    int deleteReservationById(Long id);
}
