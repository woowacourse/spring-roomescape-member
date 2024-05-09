package roomescape.domain.reservation.mapper;

import roomescape.domain.reservation.domain.ReservationTime;
import roomescape.domain.reservation.dto.TimeMemberResponse;
import roomescape.domain.reservation.dto.TimeSaveRequest;

public class TimeMapper {

    public TimeMemberResponse mapToResponse(ReservationTime reservationTime, boolean alreadyBooked) {
        return new TimeMemberResponse(reservationTime.getId(), reservationTime.getStartAt(), alreadyBooked);
    }

    public roomescape.domain.reservation.dto.TimeResponse mapToResponse(ReservationTime reservationTime) {
        return new roomescape.domain.reservation.dto.TimeResponse(reservationTime.getId(), reservationTime.getStartAt());
    }

    public roomescape.domain.reservation.dto.TimeResponse mapToResponse(Long id, ReservationTime reservationTime) {
        return new roomescape.domain.reservation.dto.TimeResponse(id, reservationTime.getStartAt());
    }

    public ReservationTime mapToTime(TimeSaveRequest request) {
        return new ReservationTime(request.id(), request.startAt());
    }
}
