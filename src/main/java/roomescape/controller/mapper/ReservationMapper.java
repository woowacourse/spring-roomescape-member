package roomescape.controller.mapper;

import org.springframework.stereotype.Component;
import roomescape.controller.dto.ReservationCreateRequest;
import roomescape.controller.dto.ReservationResponse;
import roomescape.controller.dto.ReservationTimeResponse;
import roomescape.domain.Reservation;
import roomescape.service.command.ReservationCreateCommand;

@Component
public class ReservationMapper {

    private final ReservationTimeMapper timeMapper;

    public ReservationMapper(ReservationTimeMapper timeMapper) {
        this.timeMapper = timeMapper;
    }

    public ReservationCreateCommand mapCreateToCommand(
            ReservationCreateRequest createRequest
    ) {
        return new ReservationCreateCommand(
                createRequest.name(),
                createRequest.date(),
                createRequest.timeId(),
                createRequest.themeId()
        );
    }

    public ReservationResponse mapToResponse(
            Reservation reservation
    ) {
        ReservationTimeResponse time = timeMapper.mapToResponse(reservation.getTime());

        return new ReservationResponse(
                reservation.getId(),
                reservation.getName(),
                reservation.getDate(),
                time
        );
    }
}
