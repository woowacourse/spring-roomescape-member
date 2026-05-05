package roomescape.dto;

import roomescape.domain.ReservationTime;

import java.util.List;
import java.util.Map;

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
