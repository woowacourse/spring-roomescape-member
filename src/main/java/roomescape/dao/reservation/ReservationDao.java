package roomescape.dao.reservation;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import roomescape.domain.Reservation;

public interface ReservationDao {

    List<Reservation> findAll();

    Reservation create(Reservation reservation);

    void delete(long id);

    Optional<Reservation> findByThemeAndDateAndTime(Reservation reservation);

    boolean existsById(Long id);

    List<Reservation> findByThemeAndMemberAndDate(Long themeId, Long memberId, LocalDate dateFrom, LocalDate dateTo);
}
