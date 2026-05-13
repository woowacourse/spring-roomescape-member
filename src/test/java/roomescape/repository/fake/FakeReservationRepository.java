package roomescape.repository.fake;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import roomescape.domain.Reservation;
import roomescape.global.exception.EntityNotFoundException;
import roomescape.repository.ReservationRepository;

public class FakeReservationRepository implements ReservationRepository {

    private final List<Reservation> reservations = new CopyOnWriteArrayList<>();
    private final AtomicLong counter = new AtomicLong(1);

    @Override
    public Reservation save(Reservation reservation) {
        Reservation saved = new Reservation(
                counter.getAndIncrement(),
                reservation.getName(),
                reservation.getDate(),
                reservation.getTheme(),
                reservation.getTime()
        );
        reservations.add(saved);
        return saved;
    }

    @Override
    public void deleteById(Long id) {
        boolean deleted = reservations.removeIf(reservation -> reservation.getId().equals(id));
        if (!deleted) {
            throw new EntityNotFoundException("존재하지 않는 예약 정보입니다.");
        }
    }

    @Override
    public boolean existByDateAndTimeIdAndThemeId(LocalDate date, Long timeId, Long themeId) {
        return reservations.stream()
                .anyMatch(reservation ->
                        reservation.getDate().equals(date)
                                && reservation.getTime().getId().equals(timeId)
                                && reservation.getTheme().getId().equals(themeId)
                );
    }

    @Override
    public List<Reservation> findAllByPaging(int page, int size) {
        int offset = page * size;

        return reservations.stream()
                .sorted(Comparator.comparing(Reservation::getId).reversed())
                .skip(offset)
                .limit(size)
                .toList();
    }

    @Override
    public Set<Long> findReservedTimeIdsByThemeIdAndDate(Long themeId, LocalDate date) {
        return reservations.stream()
                .filter(reservation -> reservation.getTheme().getId().equals(themeId))
                .filter(reservation -> reservation.getDate().equals(date))
                .map(reservation -> reservation.getTime().getId())
                .collect(Collectors.toSet());
    }

    @Override
    public List<Reservation> findAllByUserName(String name) {
        return reservations.stream()
                .filter(reservation -> reservation.getName().equals(name))
                .toList();
    }
}
