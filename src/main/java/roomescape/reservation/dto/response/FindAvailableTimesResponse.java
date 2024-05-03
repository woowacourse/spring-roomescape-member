package roomescape.reservation.dto.response;

import roomescape.reservationtime.model.ReservationTime;
import roomescape.util.CustomDateTimeFormatter;

public record FindAvailableTimesResponse(Long id, String startAt, Boolean alreadyBooked) {
    public static FindAvailableTimesResponse of(final ReservationTime reservationTime, final Boolean alreadyBooked) {
        return new FindAvailableTimesResponse(
                reservationTime.getId(),
                CustomDateTimeFormatter.getFormattedTime(reservationTime.getTime()),
                alreadyBooked);
    }
}
