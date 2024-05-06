package roomescape.repository.mock;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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
    public Reservation findById(final long id) {
        return reservations.stream()
                .filter(reservation -> reservation.getId() == id)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("[ERROR] 존재하지 않는 예약입니다."));
    }

    @Override
    public boolean existByDateAndTimeAndTheme(final LocalDate date, final Long timeId, final Long themeId) {
        return reservations.stream()
                .anyMatch(target -> target.getDate() == date
                                    && Objects.equals(target.getTimeId(), timeId)
                                    && Objects.equals(target.getThemeId(), themeId));

    }

    @Override
    public long save(final Reservation reservation) {
        reservations.add(reservation);
        return reservations.size();
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
}
