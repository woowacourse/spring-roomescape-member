package roomescape.dto;

import java.util.List;
import java.util.Map;
import roomescape.domain.ReservationTime;

public record AvailableTimeResponse(
        Long id,
        String time,
        Boolean available
) {
    public static List<AvailableTimeResponse> toDto(Map<ReservationTime, Boolean> reservationBooleanMap) {
        return reservationBooleanMap.entrySet().stream()
                .map(reservationBooleanEntry -> {
                    ReservationTime reservationTime = reservationBooleanEntry.getKey();
                    return new AvailableTimeResponse(
                            reservationTime.getId(),
                            reservationTime.getStartAt().toString(),
                            reservationBooleanEntry.getValue()
                    );
                }).toList();
    }
}
