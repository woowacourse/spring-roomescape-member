package roomescape.reservation.domain;

import java.util.List;

public interface ReservationRepository {

    Long save(final Reservation reservation);

    int deleteById(final Long id);

    Reservation findById(final Long id);

    List<Reservation> findAll();
}
