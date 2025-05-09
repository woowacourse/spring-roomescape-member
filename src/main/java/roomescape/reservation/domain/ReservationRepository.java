package roomescape.reservation.domain;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface ReservationRepository {

    Long save(Reservation reservation);

    void deleteById(Long id);

    boolean existsByDateAndStartAtAndThemeId(LocalDate date, LocalTime startAt, Long themeId);

    boolean existsByDateAndTimeIdAndThemeId(LocalDate date, Long timeId, Long themeId);

    Optional<Reservation> findById(Long id);

    List<Reservation> findAll();

    List<Reservation> findAllByThemIdAndMemberIdAndDateRange(Long themeId, Long memberId, LocalDate dateFrom,
                                                             LocalDate dateTo);
}
