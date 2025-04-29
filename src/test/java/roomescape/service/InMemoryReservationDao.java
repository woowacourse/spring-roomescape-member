package roomescape.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import roomescape.dao.reservation.ReservationDao;
import roomescape.dao.resetvationTime.ReservationTimeDao;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;

public class InMemoryReservationDao implements ReservationDao {

    private final List<Reservation> reservations;
    private final ReservationTimeDao reservationTimeDao;
    private final AtomicLong index = new AtomicLong(1);

    public InMemoryReservationDao(final List<Reservation> reservations, final ReservationTimeDao reservationTimeDao) {
        this.reservations = reservations;
        this.reservationTimeDao = reservationTimeDao;
    }

    @Override
    public List<Reservation> findAll() {
        return reservations;
    }

    @Override
    public Reservation create(final Reservation reservation) {
        Optional<ReservationTime> reservationTime = reservationTimeDao.findById(reservation.getTime().getId());
        Reservation savedReservation = new Reservation(index.getAndIncrement(),
                reservation.getName(),
                reservation.getDate(),
                reservationTime.get()
        );
        reservations.add(savedReservation);
        return savedReservation;
    }

    @Override
    public void delete(final long id) {
        Reservation reservation = reservations.stream()
                .filter(it -> it.isEqualId(id))
                .findFirst()
                .orElseThrow(NoSuchElementException::new);
        reservations.remove(reservation);
    }

    @Override
    public Optional<Reservation> findByDateAndTime(final Reservation reservation) {
        return reservations.stream()
                .filter(reservation1 -> reservation1.getTime().getId().equals(reservation.getTime().getId()))
                .filter(reservation1 -> reservation1.getDate().isEqual(reservation.getDate()))
                .findFirst();
    }
}
