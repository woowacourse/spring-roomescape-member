package roomescape.domain;

import java.time.LocalDate;
import java.util.List;

public interface ReservationRepository {

    List<Reservation> findAll();

    int countByReservationTimeId(Long id);

    Reservation findById(Long id);

    Reservation save(Reservation reservation);

    void deleteById(Long id);

    boolean existByDateAndTimeId(LocalDate date, Long id);

    int countByThemeId(Long id);
}
