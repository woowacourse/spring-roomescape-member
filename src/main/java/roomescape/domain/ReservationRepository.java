package roomescape.domain;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ReservationRepository {
    List<Reservation> findAll();

    Optional<Reservation> findById(Long id);

    List<Long> findTimeIdByDateAndThemeId(LocalDate date, Long themeId);

    List<Theme> findThemeWithMostPopularReservation(String startDate, String endDate);

    boolean existsByDateAndTimeIdAndThemeId(LocalDate date, Long timeId, Long themeId);

    boolean existsByTimeId(Long timeId);

    boolean existsByThemeId(Long themeId);

    Reservation save(Reservation reservation);

    void delete(Reservation reservation);
}
