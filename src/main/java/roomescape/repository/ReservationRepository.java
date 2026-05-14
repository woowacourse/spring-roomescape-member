package roomescape.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import roomescape.domain.Reservation;

public interface ReservationRepository {

    List<Reservation> findAll(int limit, int offset);

    List<Reservation> findAllByName(String name, int limit, int offset);

    Optional<Reservation> findById(Long id);

    Long save(Reservation reservation);

    int deleteById(Long id);

    List<Long> findTimeIdsByThemeIdAndDate(Long themeId, LocalDate date);

    boolean existsByDateAndTimeIdAndThemeId(LocalDate date, Long timeId, Long themeId);

    boolean existsByReservationTimeId(Long timeId);
}