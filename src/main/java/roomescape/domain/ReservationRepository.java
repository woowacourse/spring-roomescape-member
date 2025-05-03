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

    int getCountByTimeIdAndThemeIdAndDate(Long timeId, Long themeId, LocalDate date);

    ReservationTime saveReservationTime(ReservationTime reservationTime);

    List<ReservationTime> findAllReservationTimes();

    void deleteReservationTimeById(Long id);

    List<Theme> findAllThemes();

    void deleteThemeById(Long id);

    List<Theme> findPopularThemes(int count);

    int getThemeCountByName(String name);

    Theme saveTheme(Theme theme);
}
