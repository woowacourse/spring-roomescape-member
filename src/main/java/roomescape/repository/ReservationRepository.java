package roomescape.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import roomescape.domain.Reservation;

public interface ReservationRepository {

    Long save(Reservation reservation);

    List<Reservation> findAll();

    Reservation findById(Long id);

    void delete(Long id);

    boolean isTimeIdExist(Long id);

    Optional<Reservation> findByDateAndTimeId(LocalDate date, Long timeId);

}
