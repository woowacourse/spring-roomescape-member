package roomescape.reservation.repository;

import java.time.LocalDate;
import java.util.List;
import roomescape.reservation.domain.Reservation;
import roomescape.reservationtime.dto.response.AvailableReservationTimeResponse;

public interface ReservationRepository {

    List<Reservation> findAll();

    Reservation save(Reservation reservation);

    boolean deleteById(Long id);

    boolean existsByTimeId(Long id);

    boolean existsByThemeId(Long id);

    boolean existsByDateAndTimeId(final LocalDate date, final Long timeId);

    List<AvailableReservationTimeResponse> findAvailableTimesByDateAndThemeId(LocalDate date, Long themeId);
}
