package roomescape.date.repository;

import org.springframework.stereotype.Repository;
import roomescape.date.domain.ReservationDate;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReservationDateRepository {

    Optional<ReservationDate> findById(Long id);

    List<ReservationDate> findAll();

    List<ReservationDate> findAllAfterToday();

    ReservationDate save(ReservationDate reservationDate);

    boolean delete(Long id);

}
