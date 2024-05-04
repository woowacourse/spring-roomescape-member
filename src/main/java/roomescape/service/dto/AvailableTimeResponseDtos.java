package roomescape.service.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import roomescape.domain.ReservationStatus;
import roomescape.domain.ReservationTime;
import roomescape.domain.ReservationTimeStatus;

public class AvailableTimeResponseDtos {

    private final List<AvailableTimeResponseDto> availableTimeResponses;

    public AvailableTimeResponseDtos(ReservationTimeStatus timeStatus) {
        availableTimeResponses = new ArrayList<>();

        Map<ReservationTime, ReservationStatus> timesStatus = timeStatus.getTimesStatus();
        for (Map.Entry<ReservationTime, ReservationStatus> entry : timesStatus.entrySet()) {
            ReservationTime reservationTime = entry.getKey();
            ReservationStatus isBooked = entry.getValue();
            availableTimeResponses.add(new AvailableTimeResponseDto(reservationTime, isBooked.getStatus()));
        }
    }

    public List<AvailableTimeResponseDto> getAvailableTimeResponses() {
        return availableTimeResponses;
    }
}
