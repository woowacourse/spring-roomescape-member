package roomescape.dao.reservation;

import java.time.LocalDate;
import java.util.List;
import roomescape.domain.reservation.Reservation;

public interface ReservationDao {

    List<Reservation> findAllReservation();

    List<Reservation> findByThemeIdAndMemberIDAndDateFromAndDateTo(LocalDate dateFrom,
                                                                   LocalDate dateTo,
                                                                   Long themeId,
                                                                   Long memberId);

    void saveReservation(Reservation reservation);

    void deleteReservation(Long id);

    Boolean existsReservationBy(LocalDate date, Long timeId, Long themeId);
}
