package roomescape.repository;

import java.util.List;
import roomescape.domain.Reservation;

public interface ReservationRepository {

    Long save(Reservation reservation);

    List<Reservation> findAll();

    Reservation findById(Long id);

    void delete(Long id);

    boolean isTimeIdExist(Long id);
}
