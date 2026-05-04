package roomescape.dao;

import java.util.List;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.dto.ReservationRequestDto;

public interface ReservationDao {
    Reservation create(ReservationRequestDto requestDto, ReservationTime reservationTime);

    List<Reservation> readAll();

    void delete(Long id);
}
