package roomescape.reservation.dto.response;

import java.time.LocalTime;
import roomescape.util.CustomDateTimeFormatter;

public record FindAvailableTimesResponse(Long id, String startAt, Boolean alreadyBooked) {
    public static FindAvailableTimesResponse of(final Long id, final LocalTime startAt, final Boolean alreadyBooked) {
        return new FindAvailableTimesResponse(id, CustomDateTimeFormatter.getFormattedTime(startAt), alreadyBooked);
    }
}
