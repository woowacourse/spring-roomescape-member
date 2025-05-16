package roomescape.reservationtime.controller.dto;

import java.time.LocalTime;
import roomescape.reservationtime.service.dto.CreateReservationTimeServiceRequest;

public record CreateReservationTimeRequest(LocalTime startAt) {

    public CreateReservationTimeServiceRequest toCreateReservationTimeServiceRequest() {
        return new CreateReservationTimeServiceRequest(startAt);
    }
}
