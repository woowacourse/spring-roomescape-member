package roomescape.repository;

import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Repository;
import roomescape.domain.Reservation;

@Repository
public interface ReservationRepository {

    List<Reservation> findAll();

    Reservation findById(long id);

    List<Long> findByThemeIdAndDate(long themeId, LocalDate date);

    Reservation save(Reservation reservation);

    void deleteById(long id);
}
