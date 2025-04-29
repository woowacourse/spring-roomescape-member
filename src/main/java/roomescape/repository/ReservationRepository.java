package roomescape.repository;

import roomescape.domain.Reservation;

import java.util.List;
import java.util.Optional;

public interface ReservationRepository {

    Optional<Reservation> save(final Reservation reservation);

    List<Reservation> findAll();

    Optional<Reservation> findById(final Long id);

    int deleteById(final long id);
}
