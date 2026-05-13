package roomescape.repository;

import java.time.LocalDate;
import java.util.List;
import roomescape.domain.Reservation;

public interface ReservationRepository {

    Reservation create(Reservation reservationWithoutId);

    List<Reservation> readByName(String name);

    List<Reservation> readAll();

    void delete(Long id);

    boolean existByDateAndTimeIdAndThemeId(LocalDate date, Long timeId, Long themeId);

    boolean existByTimeId(Long timeId);

    boolean existByThemeId(Long themeId);
}
