package roomescape.domain;

import java.util.List;

public interface ReservationTimeRepository {

    List<ReservationTime> findAllReservationTimes();

    ReservationTime insertReservationTime(ReservationTime reservationTime);

    void deleteReservationTimeById(long id);

    boolean isExistTimeOf(String startAt);

    boolean isExistTimeOf(long timeId);

    ReservationTime findReservationTimeById(long savedId);

    List<ReservationTime> findBookedTimeForThemeAtDate(String date, long themeId);
}
