package roomescape.reservation.domain;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface ReservationRepository {

    Long save(Reservation reservation);

    boolean deleteBy(Long id);

    List<Reservation> findBy(LocalDate date, Long themeId);

    List<Reservation> findAll();

    boolean existByReservationTimeId(Long timeId);

    boolean existBy(Long themeId, LocalDate date, LocalTime time);

    boolean existByThemeId(Long themeId);

}
