package roomescape.repository;

import roomescape.domain.Reservation;
import roomescape.dto.response.ReservationResponse;

import java.util.List;

public interface ReservationRepository {
    Reservation save(Reservation reservation);

    List<Reservation> findAll();

    void deleteById(Long id);

    boolean existsByTimeId(Long id);

    List<Reservation> findByMemberAndThemeAndVisitDateBetween(Long themeId, Long memberId, String dateFrom, String dateTo);
}
