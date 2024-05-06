package roomescape.mapper;

import roomescape.domain.ReservationTime;
import roomescape.dto.TimeMemberResponse;
import roomescape.dto.TimeResponse;
import roomescape.dto.TimeSaveRequest;

public class TimeMapper {

    public TimeMemberResponse mapToResponse(ReservationTime reservationTime, boolean alreadyBooked) {
        return new TimeMemberResponse(reservationTime.getId(), reservationTime.getStartAt(), alreadyBooked);
    }

    public TimeResponse mapToResponse(ReservationTime reservationTime) {
        return new TimeResponse(reservationTime.getId(), reservationTime.getStartAt());
    }

    public TimeResponse mapToResponse(Long id, ReservationTime reservationTime) {
        return new TimeResponse(id, reservationTime.getStartAt());
    }

    public ReservationTime mapToTime(TimeSaveRequest request) {
        return new ReservationTime(request.id(), request.startAt());
    }
}
