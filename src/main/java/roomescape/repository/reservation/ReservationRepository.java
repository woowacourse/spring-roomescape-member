package roomescape.repository.reservation;

import java.time.LocalDate;
import java.util.List;
import roomescape.domain.Reservation;
import roomescape.dto.response.ReservationResponse;

public interface ReservationRepository {

    Reservation save(Reservation reservation);

    boolean hasSameReservation(String date, Long timeId, Long themeId);

    void delete(Long id);

    List<Reservation> findAll();

    List<Reservation> findByCondition(Long memberId, Long themeId, LocalDate dateFrom, LocalDate dateTo);
}
