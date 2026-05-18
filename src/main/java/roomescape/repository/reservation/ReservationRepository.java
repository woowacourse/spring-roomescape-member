package roomescape.repository.reservation;

import java.util.List;
import java.util.Optional;

import roomescape.domain.Reservation;
import roomescape.domain.vo.MemberName;

public interface ReservationRepository {

    Reservation createReservation(Reservation reservation);

    void deleteById(Long id);

    List<Reservation> findAll();

    Optional<Reservation> findById(Long id);

    boolean existsByTimeId(Long timeId);

    List<Reservation> findReservationsByName(MemberName memberName);

    void update(Reservation updatedReservation);
}
