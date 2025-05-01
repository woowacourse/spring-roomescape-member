package roomescape.reservation.domain;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface ReservationRepository {

    Long save(Reservation reservation);

    int deleteById(Long id);

    List<Reservation> findBy(LocalDate date, Long themeId);

    Reservation findById(Long id);

    List<Reservation> findAll();

    boolean existByReservationTimeId(Long timeId);

    boolean existByDateTime(LocalDate date, LocalTime time);

    boolean existByThemeId(Long themeId);

}
