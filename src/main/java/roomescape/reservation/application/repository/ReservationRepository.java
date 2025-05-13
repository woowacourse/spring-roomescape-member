package roomescape.reservation.application.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import roomescape.reservation.application.dto.CreateReservationRequest;
import roomescape.reservation.domain.Reservation;

public interface ReservationRepository {
    Reservation insert(CreateReservationRequest request);

    List<Reservation> findAllReservations();

    void delete(Long id);

    boolean existsByTimeId(Long timeId);

    boolean existsByDateTime(LocalDateTime reservationDateTime);

    boolean existsByThemeId(Long id);

    boolean existsByDateAndThemeIdAndTimeId(LocalDate date, Long timeId, Long themeId);

    List<Reservation> findAllByFilters(Long themeId, Long memberId, LocalDate dateFrom, LocalDate dateTo);
}
