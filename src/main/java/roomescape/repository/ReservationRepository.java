package roomescape.repository;

import roomescape.entity.Reservation;
import roomescape.entity.Theme;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface ReservationRepository {

    Reservation save(Reservation reservation);

    List<Reservation> findAll();

    boolean isExistByTimeId(long id);

    boolean isDuplicateDateAndTimeAndTheme(LocalDate date, LocalTime time, Theme theme);

    int deleteById(long id);

    boolean isExistByReservationId(long id);

    List<Reservation> findByMemberAndThemeAndDateRange(Long memberId, Long themeId, LocalDate dateFrom, LocalDate dateTo);
}
