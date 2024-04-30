package roomescape.console.view;

import java.util.List;
import roomescape.core.dto.ReservationRequestDto;
import roomescape.core.dto.ReservationTimeRequestDto;

public class RequestBodyMapper {
    public ReservationRequestDto mapReservationRequest(final List<String> body) {
        return new ReservationRequestDto(body.get(0), body.get(1), Long.parseLong(body.get(2)));
    }

    public ReservationTimeRequestDto mapReservationTimeRequest(final List<String> body) {
        return new ReservationTimeRequestDto(body.get(0));
    }
}
