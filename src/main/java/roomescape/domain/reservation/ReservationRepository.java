package roomescape.domain.reservation;

import java.util.List;

public interface ReservationRepository {

    Reservation save(Reservation reservation);

    List<Reservation> findAll();

    int deleteById(Long id);

    int countByTimeId(Long timeId);

    int countByReservationDateId(Long dateId);

    List<Long> findReservedTimes(Long themeId, Long dateId);

    int countByThemeId(Long id);
}
