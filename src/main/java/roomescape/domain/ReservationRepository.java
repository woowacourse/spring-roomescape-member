package roomescape.domain;

import roomescape.dto.response.ReservationResponse;

import java.time.LocalDate;
import java.util.List;

public interface ReservationRepository {
    List<ReservationResponse> findAllReservations();

    void deleteReservationById(Long id);

    ReservationTime findReservationTimeById(Long timeId);

    Theme findThemeById(Long themeId);

    Reservation saveReservation(Reservation reservation);

    boolean existByTimeIdAndThemeIdAndDate(Long timeId, Long themeId, LocalDate date);

    ReservationTime saveReservationTime(ReservationTime reservationTime);

    List<ReservationTime> findAllReservationTimes();

    int deleteReservationTimeById(Long id);

    List<Theme> findAllThemes();

    int deleteThemeById(Long id);

    List<Theme> findPopularThemes(int count);

    boolean existByName(String name);

    Theme saveTheme(Theme theme);

    List<Long> findBookedTimes(Long themeId, LocalDate date);

    boolean existReservationByThemeId(Long themeId);

    boolean existReservationByTimeId(Long timeId);
}
