package roomescape.dao;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import roomescape.domain.ReservationTime;
import roomescape.dto.response.ReservationTimeWithIsBookedGetResponse;

public interface ReservationTimeDao {

    List<ReservationTime> findAll();

    ReservationTime findById(Long id);

    ReservationTime add(ReservationTime reservationTime);

    boolean existByStartAt(LocalTime startAt);

    int deleteById(Long id);
}
