package roomescape.reservation.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import roomescape.reservation.domain.Reservation;

public interface ReservationRepository {

    Reservation save(Reservation reservation);

    void deleteById(Long id);

    void update(Reservation reservation);

    List<Reservation> findAllByName(String name);

    Optional<Reservation> findById(Long id);

    List<Reservation> findAll();

    List<PopularThemeQueryResult> findPopularThemes(LocalDate from, LocalDate to, int limit);
}
