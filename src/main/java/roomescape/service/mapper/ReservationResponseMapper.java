package roomescape.service.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import roomescape.controller.dto.ReservationDetailResponse;
import roomescape.controller.dto.ReservationSummaryResponse;
import roomescape.controller.dto.ReservationTimeResponse;
import roomescape.controller.dto.ThemeResponse;
import roomescape.domain.Reservation;
import roomescape.service.dto.AssembledReservation;

@Component
@RequiredArgsConstructor
public class ReservationResponseMapper {

    private final ReservationTimeResponseMapper timeResponseMapper;
    private final ThemeResponseMapper themeResponseMapper;

    public ReservationSummaryResponse mapToSummaryResponse(
            Reservation reservation
    ) {
        return new ReservationSummaryResponse(
                reservation.getId().getValueAsString(),
                reservation.getName(),
                reservation.getDate(),
                reservation.isCanceled(),
                reservation.getTimeId().getValueAsString(),
                reservation.getThemeId().getValueAsString()
        );
    }

    public ReservationDetailResponse mapToDetailResponse(
            AssembledReservation assembledReservation,
            boolean cancelable
    ) {
        Reservation reservation = assembledReservation.reservation();
        ReservationTimeResponse time = timeResponseMapper.map(assembledReservation.time());
        ThemeResponse theme = themeResponseMapper.map(assembledReservation.theme());

        return new ReservationDetailResponse(
                reservation.getId().getValueAsString(),
                reservation.getName(),
                reservation.getDate(),
                reservation.isCanceled(),
                cancelable,
                time,
                theme
        );
    }
}
