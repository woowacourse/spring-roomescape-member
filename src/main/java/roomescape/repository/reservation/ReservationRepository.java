package roomescape.repository.reservation;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import roomescape.domain.Reservation;
import roomescape.dto.other.ReservationSearchCondition;

public interface ReservationRepository {

    List<Reservation> findAll();

    List<Reservation> findAllByFilter(ReservationSearchCondition condition);

    Optional<Reservation> findById(long id);

    boolean checkAlreadyReserved(LocalDate date, long timeId, long themeId);

    boolean checkExistenceInTime(long reservationTimeId);

    boolean checkExistenceInTheme(long themeId);

    long add(Reservation reservation);

    void deleteById(long id);
}
