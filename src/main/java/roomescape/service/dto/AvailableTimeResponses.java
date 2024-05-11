package roomescape.service.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonCreator.Mode;
import java.util.List;
import roomescape.domain.ReservationTimeStatuses;

public class AvailableTimeResponses {

    private final List<AvailableTimeResponse> availableTimeResponses;

    @JsonCreator(mode = Mode.PROPERTIES)
    public AvailableTimeResponses(List<AvailableTimeResponse> availableTimeResponses) {
        this.availableTimeResponses = availableTimeResponses;
    }

    public AvailableTimeResponses(ReservationTimeStatuses reservationTimeStatuses) {
        this(reservationTimeStatuses.getTimeStatuses().stream()
                .map(AvailableTimeResponse::new)
                .toList());
    }

    public List<AvailableTimeResponse> getAvailableTimeResponses() {
        return availableTimeResponses;
    }
}
