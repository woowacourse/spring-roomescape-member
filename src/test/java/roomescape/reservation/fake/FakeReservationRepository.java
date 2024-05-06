package roomescape.reservation.fake;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import roomescape.reservation.model.Reservation;
import roomescape.reservation.repository.ReservationRepository;

@Repository
@Qualifier("fakeReservationRepository")
public class FakeReservationRepository implements ReservationRepository {

    private final List<Reservation> db = new ArrayList<>();
    AtomicLong count = new AtomicLong();

    @Override
    public Reservation save(Reservation reservation) {
        Reservation saveReservation = new Reservation(
                count.incrementAndGet(),
                reservation.getName(),
                reservation.getDate(),
                reservation.getReservationTime(),
                reservation.getTheme()
        );
        db.add(saveReservation);
        return saveReservation;
    }

    @Override
    public List<Reservation> findAll() {
        return db;
    }

    @Override
    public Optional<Reservation> findById(Long id) {
        return db.stream()
                .filter(reservation -> reservation.getId().equals(id))
                .findAny();
    }

    @Override
    public List<Reservation> findAllByTimeId(Long timeId) {
        return db.stream()
                .filter(reservation -> reservation.getReservationTime().getId().equals(timeId))
                .toList();
    }

    @Override
    public List<Reservation> findAllByDateAndThemeId(LocalDate date, Long themeId) {
        return db.stream()
                .filter(reservation ->
                        reservation.getDate().equals(date) && reservation.getTheme().getId().equals(themeId))
                .toList();
    }

    @Override
    public boolean existsByDateAndTimeAndTheme(LocalDate date, Long timeId, Long themeId) {
        Optional<Reservation> findReservation = findAllByDateAndThemeId(date, themeId).stream()
                .filter(reservation -> reservation.getReservationTime().getId().equals(timeId))
                .findAny();
        return findReservation.isPresent();
    }

    @Override
    public void deleteById(Long id) {
        findById(id).ifPresent(db::remove);
    }

    @Override
    public List<Reservation> findAllByThemeId(Long themeId) {
        return db.stream()
                .filter(reservation -> reservation.getTheme().getId().equals(themeId))
                .toList();
    }

    public void clear() {
        db.clear();
        count.set(0);
    }
}
