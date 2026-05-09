package roomescape.dto.reservationTime;

import roomescape.domain.ReservationTime;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

public record AvailableReservationTimesResponseDto(
        List<AvailableReservationTimeResponseDto> times
) {

    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    public static AvailableReservationTimesResponseDto of(Map<ReservationTime, Boolean> timesWithAvailability) {
        return new AvailableReservationTimesResponseDto(timesWithAvailability.keySet().stream()
                .map(time ->
                        new AvailableReservationTimeResponseDto(
                                time.getId(),
                                TIME_FORMATTER.format(time.getStartAt()),
                                timesWithAvailability.get(time)))
                .toList());
    }
}