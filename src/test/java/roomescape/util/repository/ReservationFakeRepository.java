package roomescape.util.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.repository.ReservationRepository;

public class ReservationFakeRepository implements ReservationRepository {

    private final Map<Long, Reservation> reservations = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong();

    @Override
    public Long saveAndReturnId(Reservation reservation) {
        Long id = idGenerator.incrementAndGet();
        reservations.put(id, reservation.createWithId(id));
        return id;
    }

    @Override
    public int deleteById(Long id) {
        if (reservations.containsKey(id)) {
            reservations.remove(id);
            return 1;
        }
        return 0;
    }

    @Override
    public List<Reservation> findAll() {
        return reservations.values().stream().toList();
    }

    @Override
    public Boolean existReservationByTimeId(Long timeId) {
        return reservations.values().stream()
                .map(reservation -> reservation.getTime().getId())
                .anyMatch(id -> id.equals(timeId));
    }

    @Override
    public Boolean existReservationByDateAndTimeIdAndThemeId(LocalDate date, Long timeId, Long themeId) {
        return reservations.values().stream()
                .anyMatch(reservation -> reservation.getDate().equals(date)
                        && reservation.getTime().getId().equals(timeId)
                        && reservation.getTheme().getId().equals(themeId));
    }

    @Override
    public Boolean existReservationByThemeId(Long themeId) {
        return reservations.values().stream()
                .map(reservation -> reservation.getTheme().getId())
                .anyMatch(id -> id.equals(themeId));
    }

    @Override
    public List<Reservation> findAllByDateAndThemeId(LocalDate date, Long themeId) {
        return reservations.values().stream()
                .filter(reservation -> reservation.getDate().equals(date))
                .filter(reservation -> reservation.getTheme().getId().equals(themeId))
                .toList();
    }

    @Override
    public List<Reservation> findAllByThemeIdAndMemberIdAndPeriod(Long themeId, Long memberId, LocalDate dateFrom,
                                                                  LocalDate dateTo) {
        return reservations.values().stream()
                .filter(reservation -> reservation.getTheme().getId().equals(themeId))
                .filter(reservation -> reservation.getMember().getId().equals(memberId))
                .filter(reservation -> reservation.getDate().equals(dateFrom))
                .filter(reservation -> reservation.getDate().equals(dateTo))
                .toList();
    }
}
