package roomescape.reservation.domain;

import java.time.LocalDate;
import java.util.List;

public interface ReservationRepository {

    List<Reservation> findAll();

    Reservation findByReservationId(Long id);

    List<Reservation> findByDateAndThemeId(LocalDate date, Long id);

    List<Reservation> findByIdsAndDates(Long memberId, Long themeId, LocalDate from, LocalDate to);

    Reservation save(Reservation reservation);

    int deleteById(Long id);
}
