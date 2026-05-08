package roomescape.service.fake;

import java.time.LocalDate;
import roomescape.dao.ReservationDao;
import roomescape.domain.Reservation;

public class FakeReservationDao extends FakeCommonDao<Reservation> implements ReservationDao {

    @Override
    public boolean existsByThemeIdAndTimeIdAndDate(Long themeId, Long timeId, LocalDate date) {
        return store.values()
                .stream()
                .anyMatch(reservation ->
                        reservation.getTheme().getId().equals(themeId)
                                && reservation.getTime().getId().equals(timeId)
                                && reservation.getDate().equals(date));
    }

    @Override
    public boolean existsByThemeId(Long themeId) {
        return store.values()
                .stream()
                .anyMatch(reservation -> reservation.getTheme().getId().equals(themeId));
    }

    @Override
    public boolean existsByTimeId(Long timeId) {
        return store.values()
                .stream()
                .anyMatch(reservation -> reservation.getTime().getId().equals(timeId));
    }

    @Override
    Reservation create(Reservation reservation, Long id) {
        return new Reservation(id, reservation.getName(), reservation.getDate(), reservation.getTime(),
                reservation.getTheme());
    }
}
