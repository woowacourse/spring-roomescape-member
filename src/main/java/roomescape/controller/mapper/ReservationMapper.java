package roomescape.controller.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import roomescape.controller.dto.ReservationCreateRequest;
import roomescape.controller.dto.ReservationDetailResponse;
import roomescape.controller.dto.ReservationSummaryResponse;
import roomescape.controller.dto.ReservationTimeResponse;
import roomescape.controller.dto.ThemeResponse;
import roomescape.domain.EntityId;
import roomescape.domain.Reservation;
import roomescape.service.dto.AssembledReservation;
import roomescape.service.dto.ReservationCreateCommand;

@Component
@RequiredArgsConstructor
public class ReservationMapper {

    private final ReservationTimeMapper timeMapper;
    private final ThemeMapper themeMapper;

    public ReservationCreateCommand mapToCommand(
            ReservationCreateRequest request
    ) {
        return new ReservationCreateCommand(
                request.name(),
                request.date(),
                EntityId.fromString(request.timeId().toString()),
                EntityId.fromString(request.themeId().toString())
        );
    }

    public ReservationSummaryResponse mapToSummaryResponse(
            Reservation reservation
    ) {
        return new ReservationSummaryResponse(
                reservation.id().getValueAsString(),
                reservation.name(),
                reservation.date(),
                reservation.timeId().getValueAsString(),
                reservation.themeId().getValueAsString()
        );
    }

    public ReservationDetailResponse mapToDetailResponse(
            AssembledReservation assembledReservation
    ) {
        Reservation reservation = assembledReservation.reservation();
        ReservationTimeResponse time = timeMapper.mapToResponse(assembledReservation.time());
        ThemeResponse theme = themeMapper.mapToResponse(assembledReservation.theme());

        return new ReservationDetailResponse(
                reservation.id().getValueAsString(),
                reservation.name(),
                reservation.date(),
                time,
                theme
        );
    }
}
