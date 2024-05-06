package roomescape.service.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonCreator.Mode;
import java.util.List;
import roomescape.domain.ReservationTimeStatuses;

public class AvailableTimeResponseDtos {

    private final List<AvailableTimeResponseDto> availableTimeResponses;

    @JsonCreator(mode = Mode.PROPERTIES)
    public AvailableTimeResponseDtos(List<AvailableTimeResponseDto> availableTimeResponses) {
        this.availableTimeResponses = availableTimeResponses;
    }

    public AvailableTimeResponseDtos(ReservationTimeStatuses reservationTimeStatuses) {
        this(reservationTimeStatuses.getTimeStatuses().stream()
                .map(AvailableTimeResponseDto::new)
                .toList());
    }

    public List<AvailableTimeResponseDto> getAvailableTimeResponses() {
        return availableTimeResponses;
    }
}
