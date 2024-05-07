package roomescape.theme.fake;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import roomescape.reservation.model.Reservation;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.theme.model.Theme;
import roomescape.theme.repository.ThemeRepository;

@Repository
public class FakeThemeRepository implements ThemeRepository {

    private final ReservationRepository reservationRepository;
    private final List<Theme> db = new ArrayList<>();
    private final AtomicLong count = new AtomicLong();


    public FakeThemeRepository(@Qualifier("fakeReservationRepository") ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    @Override
    public Theme save(Theme theme) {
        Theme saveReservation = new Theme(
                count.incrementAndGet(),
                theme.getName(),
                theme.getDescription(),
                theme.getThumbnail()
        );
        db.add(saveReservation);
        return saveReservation;
    }

    @Override
    public List<Theme> findAll() {
        return db;
    }

    @Override
    public Optional<Theme> findById(Long id) {
        return db.stream()
                .filter(theme -> theme.getId().equals(id))
                .findAny();
    }

    @Override
    public List<Theme> findOrderByReservation() {
        return reservationRepository.findAll().stream()
                .collect(Collectors.groupingBy(Reservation::getTheme, Collectors.counting())).entrySet().stream()
                .sorted(Collections.reverseOrder(Entry.comparingByValue()))
                .map(Entry::getKey)
                .limit(10)
                .toList();
    }

    @Override
    public void deleteById(Long id) {
        findById(id).ifPresent(db::remove);
    }

    public void clear() {
        db.clear();
        count.set(0);
    }
}
