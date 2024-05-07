package roomescape.repository.dao;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import roomescape.model.ReservationTime;

public interface ReservationTimeDao {
    List<ReservationTime> findAllReservationTimes();

    List<ReservationTime> findAllReservedTimes(LocalDate date, long themeId);

    ReservationTime findReservationById(long id);

    ReservationTime addReservationTime(ReservationTime reservationTime);

    void deleteReservationTime(long id);

    Long countReservationTimeById(long id);

    Long countReservationTimeByStartAt(LocalTime startAt);
}
