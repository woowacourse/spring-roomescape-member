package roomescape.domain;

import java.time.LocalDate;
import java.util.List;

public interface ReservationRepository {

    List<Reservation> findAll();

    List<Reservation> findAllByReservationTimeId(Long id);

    Reservation findById(Long id);

    Reservation save(Reservation reservation);

    void deleteById(Long id);

    boolean existByDateAndTimeId(LocalDate date, Long id);
}
