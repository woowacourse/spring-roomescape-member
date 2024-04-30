package roomescape.repository;

import roomescape.model.Reservation;

import java.util.List;

public interface ReservationRepository {

    Reservation save(final Reservation reservation);

    List<Reservation> findAll();

    boolean deleteById(final Long id);
}
