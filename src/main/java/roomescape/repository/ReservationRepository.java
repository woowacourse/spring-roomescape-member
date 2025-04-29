package roomescape.repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import roomescape.entity.Reservation;

public interface ReservationRepository {

    Reservation findById(Long id);

    List<Reservation> findAll();

    Reservation save(Reservation reservation);

    int deleteById(Long id);

    boolean isDuplicateDateAndTime(LocalDate date, LocalTime time);

    boolean existByTimeId(Long id);
}
