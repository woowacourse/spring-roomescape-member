package roomescape.reservation.stub;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.repository.ReservationRepository;

public class StubReservationRepository implements ReservationRepository {

    private final List<Reservation> data = new ArrayList<>();
    private final AtomicLong atomicLong = new AtomicLong();

    public StubReservationRepository(Reservation... inputReservations) {
        data.addAll(List.of(inputReservations));
        long maxId = data.stream()
                .mapToLong(Reservation::getId)
                .max()
                .orElse(0L);
        atomicLong.set(maxId);
    }

    @Override
    public List<Reservation> findAll() {
        return List.copyOf(data);
    }

    @Override
    public boolean existsByDateAndTime(LocalDate date, LocalTime time) {
        return data.stream()
                .anyMatch(reservation -> reservation.getDate().equals(date) && reservation.getTime().getStartAt()
                        .equals(time));
    }

    @Override
    public Reservation save(Reservation reservation) {
        long newId = atomicLong.incrementAndGet();
        Reservation newReservation = new Reservation(
                newId,
                reservation.getName(),
                reservation.getDate(),
                reservation.getTime()
        );
        data.add(newReservation);
        return newReservation;
    }

    @Override
    public void deleteById(Long id) {
        data.removeIf(r -> r.getId().equals(id));
    }

    @Override
    public Optional<Reservation> findById(Long id) {
        return data.stream()
                .filter(r -> r.getId().equals(id))
                .findFirst();
    }
}
