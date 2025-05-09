package roomescape.reservation.domain;

import java.time.LocalDate;
import java.util.List;

public interface ReservationRepository {

    Long save(Reservation reservation);

    boolean deleteBy(Long id);

    List<Reservation> findByDateAndThemeId(LocalDate date, Long themeId);

    List<Reservation> findAll();

    boolean existByReservationTimeId(Long timeId);

    boolean hasSameReservation(Reservation reservation);

    boolean existByThemeId(Long themeId);

}
