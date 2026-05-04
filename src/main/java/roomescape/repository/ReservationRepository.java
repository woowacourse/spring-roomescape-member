package roomescape.repository;

import org.springframework.stereotype.Repository;
import roomescape.domain.Reservation;

import java.util.List;

@Repository
public interface ReservationRepository {

    List<Reservation> findAll();

    Reservation findById(long id);

    Reservation save(Reservation reservation);

    void deleteById(long id);
}
