package roomescape.dto.reservationTime;

import roomescape.domain.ReservationTime;

import java.time.format.DateTimeFormatter;
import java.util.List;

public record AvailableReservationTimesResponseDto(
        List<AvailableReservationTimeResponseDto> times
) {

    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    public static AvailableReservationTimesResponseDto of(List<ReservationTime> availableTimes, List<ReservationTime> allTimes) {
        return new AvailableReservationTimesResponseDto(
                allTimes.stream()
                        .map(time -> {
                            boolean available = availableTimes.contains(time);
                            return new AvailableReservationTimeResponseDto(
                                    time.getId(),
                                    TIME_FORMATTER.format(time.getStartAt()),
                                    available);
                        }).toList());
    }
}
