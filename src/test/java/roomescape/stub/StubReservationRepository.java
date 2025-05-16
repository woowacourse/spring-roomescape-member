package roomescape.stub;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import lombok.Setter;
import roomescape.domain.entity.Reservation;
import roomescape.domain.repository.ReservationRepository;

public class StubReservationRepository implements ReservationRepository {

    private final List<Reservation> data = new ArrayList<>();
    private final AtomicLong idSequence = new AtomicLong();
    @Setter
    private boolean existsByReservationTimeId = false;
    @Setter
    private boolean existsByThemeId = false;

    public StubReservationRepository(Reservation... initialReservations) {
        data.addAll(List.of(initialReservations));
        long maxId = data.stream()
                .mapToLong(Reservation::getId)
                .max()
                .orElse(0L);
        idSequence.set(maxId);
    }

    @Override
    public List<Reservation> findAll() {
        return List.copyOf(data);
    }

    @Override
    public boolean existsByDateAndTimeAndTheme(LocalDate date, LocalTime time, Long themeId) {
        return data.stream()
                .anyMatch(reservation -> reservation.getDate().equals(date)
                        && reservation.getTime().getStartAt().equals(time)
                        && reservation.getTheme().getId().equals(themeId));
    }

    @Override
    public Reservation save(Reservation reservation) {
        long newId = idSequence.incrementAndGet();
        Reservation newReservation = new Reservation(
                newId,
                reservation.getMember(),
                reservation.getDate(),
                reservation.getTime(),
                reservation.getTheme()
        );
        data.add(newReservation);
        return newReservation;
    }

    @Override
    public void deleteById(Long id) {
        data.removeIf(r -> r.getId().equals(id));
    }

    @Override
    public List<Reservation> search(final Long themeId, final Long memberId, final LocalDate dateFrom, final LocalDate dateTo) {
        return data.stream()
                .filter(r -> (themeId == null || r.getTheme().getId().equals(themeId)) &&
                        (memberId == null || r.getMember().getId().equals(memberId)) &&
                        (dateFrom == null || !r.getDate().isBefore(dateFrom)) &&
                        (dateTo == null || !r.getDate().isAfter(dateTo)))
                .toList();
    }

    @Override
    public Optional<Reservation> findById(Long id) {
        return data.stream()
                .filter(r -> r.getId().equals(id))
                .findFirst();
    }

    @Override
    public boolean existsByReservationTimeId(Long id) {
        return existsByReservationTimeId;
    }

    @Override
    public boolean existsByThemeId(Long id) {
        return existsByThemeId;
    }
}
