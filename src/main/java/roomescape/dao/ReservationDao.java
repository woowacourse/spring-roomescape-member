package roomescape.dao;

import java.util.List;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.dto.ReservationRequestDto;

public interface ReservationDao {
    Reservation create(ReservationRequestDto requestDto, ReservationTime reservationTime, Theme theme);

    List<Reservation> readAll();

    void delete(Long id);
}
