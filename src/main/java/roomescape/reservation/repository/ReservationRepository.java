package roomescape.reservation.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import roomescape.reservation.domain.Reservation;

public interface ReservationRepository {
    List<Reservation> findAll();

    Optional<Reservation> findById(Long id);

    Long save(Reservation reservation);

    void delete(Long id);

    // TODO: 시그니처 변경, ThemeId 추가 (테마까지 같은지 비교)
    boolean existsByDateAndTimeId(LocalDate date, Long timeId);
}
