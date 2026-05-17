package roomescape.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import roomescape.domain.Reservation;

public interface ReservationRepository {

    Reservation save(Reservation reservation);

    List<Reservation> findAll();

    List<Reservation> findAllByUsername(String username);

    Optional<Reservation> findById(Long id);

    int update(Reservation reservation);

    void deleteById(Long id);

    int deleteReservationWith(String name, LocalDate date, Long timeId, Long themeId);

    boolean existReservationByTimeId(Long timeId);

    boolean existsReservationWith(LocalDate date, Long timeId, Long themeId);
}
