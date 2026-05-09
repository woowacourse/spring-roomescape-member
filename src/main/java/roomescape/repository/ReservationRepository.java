package roomescape.repository;

import org.springframework.stereotype.Repository;
import roomescape.domain.Reservation;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReservationRepository {

    List<Reservation> findAll();

    Optional<Reservation> findById(long id);

    List<Long> findByThemeIdAndDate(long themeId, LocalDate date);

    Reservation save(Reservation reservation);

    void deleteById(long id);

    boolean existsByDateAndTimeIdAndThemeId(LocalDate date, Long timeId, Long themeId);
}
