package roomescape.domain;

import java.time.LocalDate;
import java.util.List;

public interface ReservationRepository {

    List<Reservation> findAll();

    Reservation findById(Long id);

    Reservation save(Reservation reservation);

    void deleteById(Long id);

    boolean existByReservationTimeId(Long id);

    boolean existByThemeId(Long id);

    boolean existByDateAndTimeId(LocalDate date, Long id);
}
