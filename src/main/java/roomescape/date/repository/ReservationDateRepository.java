package roomescape.date.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;
import roomescape.date.domain.ReservationDate;

@Repository
public interface ReservationDateRepository {

    Optional<ReservationDate> findById(Long id);

    List<ReservationDate> findAll();

    List<ReservationDate> findAllAfterToday();

    ReservationDate save(ReservationDate reservationDate);

    boolean delete(Long id);

}
