package roomescape.repository;

import roomescape.domain.Reservation;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ReservationRepository {

    List<Reservation> findAll();

    List<Reservation> findAllByDateAndThemeId(LocalDate date, long themeId);

    List<Reservation> findAllByThemeIdAndMemberIdAndDateRange(Long themeId, Long memberId, String dateFrom, String dateTo);

    Optional<Reservation> findById(long id);

    boolean existsByTimeId(long timeId);

    boolean existsByThemeId(long themeId);

    boolean existsByDateAndTimeIdAndThemeId(LocalDate date, long timeId, long themeId);

    Reservation save(Reservation reservation);

    int delete(long id);

    int deleteByMemberId(long id, long memberId);
}
