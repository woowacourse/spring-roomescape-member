package roomescape.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import roomescape.domain.Reservation;

public interface ReservationRepository {
    List<Reservation> findAll();

    Optional<Reservation> findById(long id);

    boolean checkAlreadyReserved(LocalDate date, long timeId, long themeId);

    boolean checkExistenceInTime(long reservationTimeId);

    boolean checkExistenceInTheme(long themeId);

    long add(Reservation reservation);

    void deleteById(long id);

    List<Reservation> saerch(Long themeId, Long memberId, LocalDate dateFrom, LocalDate dateTo);
}
