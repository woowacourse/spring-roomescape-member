package roomescape.reservation.repository;

import java.time.LocalDate;
import java.util.List;
import roomescape.reservation.domain.Reservation;

public interface ReservationRepository {

    Reservation save(Reservation reservation);

    void deleteById(Long id);

    List<Reservation> findAllByName(String name);

    List<Reservation> findAll();

    List<PopularThemeQueryResult> findPopularThemes(LocalDate from, LocalDate to, int limit);
}
