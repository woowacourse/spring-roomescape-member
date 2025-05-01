package roomescape.reservation.repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.repository.entity.ReservationEntity;

public interface ReservationRepository {

    Long save(ReservationEntity reservationEntity);

    Optional<Reservation> findById(Long id);

    long countByDateAndTimeIdAndThemeId(LocalDate date, Long timeId, Long themeId);

    boolean existsByDateAndStartAtAndThemeId(LocalDate date, LocalTime startAt, Long themeId);

    List<Reservation> findAll();

    void deleteById(Long id);
}
