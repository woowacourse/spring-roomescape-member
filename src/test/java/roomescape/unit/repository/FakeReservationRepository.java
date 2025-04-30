package roomescape.unit.repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import roomescape.domain.Reservation;
import roomescape.exception.InvalidReservationException;
import roomescape.repository.ReservationRepository;

public class FakeReservationRepository implements ReservationRepository {

    AtomicLong index = new AtomicLong(1L);
    List<Reservation> reservations = new ArrayList<>();

    @Override
    public long add(Reservation reservation) {
        long id = index.getAndIncrement();
        reservations.add(
                new Reservation(id, reservation.getName(), reservation.getDate(), reservation.getReservationTime(),
                        reservation.getTheme()));
        return id;
    }

    @Override
    public List<Reservation> findAll() {
        return Collections.unmodifiableList(reservations);
    }

    @Override
    public void deleteById(Long id) {
        Optional<Reservation> findReservation = reservations.stream()
                .filter(reservation -> reservation.getId().equals(id))
                .findAny();
        findReservation.orElseThrow(() -> new InvalidReservationException("존재하지 않는 id입니다." + id));
        reservations.remove(findReservation.get());
    }

    @Override
    public boolean existsByTimeId(Long id) {
        return reservations.stream()
                .anyMatch(reservation -> reservation.getReservationTime().getId().equals(id));
    }

    @Override
    public boolean existsByDateAndTimeId(Reservation reservation) {
        return reservations.stream()
                .anyMatch(currentReservation -> currentReservation.getDate().isEqual(reservation.getDate())
                        && currentReservation.getReservationTime().getId()
                        .equals(reservation.getReservationTime().getId()));
    }

    @Override
    public List<Reservation> findAllByDateAndThemeId(LocalDate date, Long themeId) {
        return reservations.stream()
                .filter((reservation) -> reservation.getDate().isEqual(date))
                .filter((reservation) -> reservation.getTheme().getId().equals(themeId))
                .toList();
    }
}
