package roomescape.support.fake;

import java.time.LocalDate;
import java.util.List;
import roomescape.domain.reservation.Reservation;
import roomescape.domain.reservation.ReservationRepository;
import roomescape.domain.theme.Theme;

public class FakeReservationRepository implements ReservationRepository {

    public Reservation savedReservation;
    public List<Reservation> findAllResult = List.of();
    public int countByTimeIdResult;
    public int countByReservationDateIdResult;
    public int countByThemeIdResult;

    @Override
    public Reservation save(Reservation reservation) {
        savedReservation = reservation;
        return Reservation.of(
            1L,
            reservation.getName(),
            reservation.getDate(),
            reservation.getTime(),
            reservation.getTheme()
        );
    }

    @Override
    public List<Reservation> findAll() {
        return findAllResult;
    }

    @Override
    public int deleteById(Long id) {
        return 0;
    }

    @Override
    public int countByTimeId(Long timeId) {
        return countByTimeIdResult;
    }

    @Override
    public int countByReservationDateId(Long dateId) {
        return countByReservationDateIdResult;
    }

    @Override
    public List<Long> findReservedTimes(Long themeId, Long dateId) {
        return List.of();
    }

    @Override
    public List<Theme> findPopularThemes(int rankLimit, LocalDate startDay, LocalDate endDay) {
        return List.of();
    }

    @Override
    public int countByThemeId(Long id) {
        return countByThemeIdResult;
    }
}
