package roomescape.repository;

import java.time.LocalDate;
import java.util.List;
import roomescape.entity.Reservation;

public interface ReservationRepository {

    Reservation findById(Long id);

    List<Reservation> findAll();

    Reservation save(Reservation reservation);

    int deleteById(Long id);

    boolean isDuplicateDateAndTime(LocalDate date, Long timeId);

    boolean existByTimeId(Long id);
}
