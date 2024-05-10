package roomescape.repository.mock;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import roomescape.domain.Reservation;
import roomescape.repository.ReservationDao;

public class InMemoryReservationDao implements ReservationDao {

    public List<Reservation> reservations;

    public InMemoryReservationDao() {
        this.reservations = new ArrayList<>();
    }

    @Override
    public List<Reservation> findAll() {
        return reservations;
    }

    @Override
    public List<Reservation> findAll(final Long memberId, final Long themeId, final LocalDate dateFrom,
                                     final LocalDate dateTo) {
        return List.of();
    }

    @Override
    public Reservation findById(final long id) {
        return reservations.stream()
                .filter(reservation -> reservation.getId() == id)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("[ERROR] 존재하지 않는 예약입니다."));
    }

    @Override
    public boolean existByDateAndTimeAndTheme(final LocalDate date, final Long timeId, final Long themeId) {
        return reservations.stream()
                .anyMatch(target -> target.getDate().equals(date)
                                    && target.getTimeId().equals(timeId)
                                    && target.getThemeId().equals(themeId));

    }

    @Override
    public Reservation save(final Reservation reservation) {
        reservations.add(reservation);
        return reservations.get(reservations.size() - 1);
    }

    @Override
    public void deleteById(final Long id) {
        Reservation reservation = reservations.stream()
                .filter(target -> target.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("[ERROR] 존재하지 않는 예약 시간입니다."));

        reservations.remove(reservation);
    }

    @Override
    public List<Long> findTimeIdsByDateAndThemeId(final LocalDate date, final Long themeId) {
        return reservations.stream()
                .filter(reservation -> reservation.getDate().equals(date)
                                       && reservation.getThemeId().equals(themeId))
                .mapToLong(Reservation::getTimeId)
                .boxed().toList();
    }

    @Override
    public boolean existByTimeId(final Long timeId) {
        return reservations.stream()
                .anyMatch(target -> target.getTimeId().equals(timeId));
    }

    @Override
    public boolean existByThemeId(final Long themeId) {
        return reservations.stream()
                .anyMatch(target -> target.getThemeId().equals(themeId));
    }
}
