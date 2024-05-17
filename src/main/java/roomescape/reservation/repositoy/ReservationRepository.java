package roomescape.reservation.repositoy;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import roomescape.reservation.domain.Reservation;

public interface ReservationRepository {

  List<Reservation> findAll(LocalDate dateFrom, LocalDate dateTo, Long themeId,
      Long memberId);

  Optional<Reservation> findById(Long reservationId);

  Reservation save(Reservation reservation);

  void deleteById(Long reservationId);

  boolean existByDateAndTimeIdAndThemeId(LocalDate date, Long timeId, Long themeId);

  boolean existByTimeId(Long reservationTimeId);

  List<Reservation> findAllByDateAndThemeId(LocalDate date, Long themeId);
}
