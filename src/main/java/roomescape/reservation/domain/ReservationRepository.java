package roomescape.reservation.domain;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface ReservationRepository {

    Long save(Reservation reservation);

    void deleteById(Long id);

    Boolean existsByDateAndStartAtAndThemeId(LocalDate date, LocalTime startAt, Long themeId);

    Boolean existsByDateAndTimeIdAndThemeId(LocalDate date, Long timeId, Long themeId);

    Optional<Reservation> findById(Long id);

    List<Reservation> findAll();

    List<Reservation> findAllByThemeIdAndMemberIdAndDateRange(Long themeId, Long memberId, LocalDate dateFrom,
                                                              LocalDate dateTo);

    List<Reservation> findReservationsByDateAndThemeId(LocalDate date, Long themeId);
}
