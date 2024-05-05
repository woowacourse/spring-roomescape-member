package roomescape.service.dto;

import java.time.LocalTime;
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
        addAvailableTimeResponses(timeStatus);
        sortAscendingByStartAt();
    }

    private void addAvailableTimeResponses(ReservationTimeStatus timeStatus) {
        Map<ReservationTime, ReservationStatus> timesStatus = timeStatus.getTimesStatus();
        for (Map.Entry<ReservationTime, ReservationStatus> entry : timesStatus.entrySet()) {
            availableTimeResponses.add(new AvailableTimeResponseDto(entry.getKey(), entry.getValue().getStatus()));
        }
    }

    private void sortAscendingByStartAt() {
        availableTimeResponses.sort((o1, o2) -> {
            LocalTime startAt1 = LocalTime.parse(o1.getTimeResponseDto().getStartAt());
            LocalTime startAt2 = LocalTime.parse(o2.getTimeResponseDto().getStartAt());
            return startAt1.compareTo(startAt2);
        });
    }

    public List<AvailableTimeResponseDto> getAvailableTimeResponses() {
        return availableTimeResponses;
    }
}
