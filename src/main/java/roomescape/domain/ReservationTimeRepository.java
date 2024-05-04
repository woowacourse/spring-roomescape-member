package roomescape.domain;

import java.util.List;

public interface ReservationTimeRepository {

    List<ReservationTime> findAllReservationTimes();

    ReservationTime insertReservationTime(ReservationTime reservationTime);

    void deleteReservationTimeById(long id);

    boolean isTimeExistsByStartTime(String startAt);

    boolean isTimeExistsByTimeId(long timeId);

    ReservationTime findReservationTimeById(long savedId);

    List<ReservationTime> findReservedTimeByThemeAndDate(String date, long themeId);
}
