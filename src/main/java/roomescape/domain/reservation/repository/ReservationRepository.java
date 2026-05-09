package roomescape.domain.reservation.repository;

import org.springframework.stereotype.Repository;
import roomescape.domain.reservation.domain.Reservation;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface ReservationRepository {

    List<Reservation> findAll();

    Reservation save(Reservation reservation);

    void deleteById(Long id);

    boolean existsByTimeId(Long timeId);

    Optional<Reservation> findById(Long id);

    boolean existsByThemeId(Long themeId);


    boolean existsBy(LocalDate date, Long timeId, Long themeId);

    Set<Long> findReservedTimeIdsByDateAndThemeId(LocalDate date, Long themeId);
}
