package roomescape.dto.reservationTime;

import java.time.format.DateTimeFormatter;
import java.util.List;
import roomescape.domain.ReservationTime;

public record AvailableReservationTimesResponse(
    List<AvailableReservationTimeResponse> times
) {

    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    public static AvailableReservationTimesResponse of(
        List<ReservationTime> allTimes,
        List<ReservationTime> availableTimes
    ) {
        return new AvailableReservationTimesResponse(
            allTimes.stream()
                .map(time -> {
                    boolean available = availableTimes.contains(time);
                    return new AvailableReservationTimeResponse(
                        time.getId(),
                        TIME_FORMATTER.format(time.getStartAt()),
                        available);
                }).toList());
    }
}
