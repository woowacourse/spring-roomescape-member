package roomescape.domain;

import java.time.LocalDate;
import java.util.List;

public interface ReservationRepository {

    Reservation save(Reservation reservation);

    List<Reservation> findAllReservations();

    List<Reservation> findReservationsByDateAndThemeId(LocalDate date, Long themeId);

    boolean existByTimeIdAndDate(Long id, LocalDate date);

    void delete(Long id);
}
