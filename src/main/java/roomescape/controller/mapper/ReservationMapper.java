package roomescape.controller.mapper;

import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;
import roomescape.controller.dto.ReservationResponse;
import roomescape.controller.dto.ReservationTimeResponse;
import roomescape.controller.dto.ThemeResponse;
import roomescape.domain.Reservation;

@Component
@RequiredArgsConstructor
public class ReservationMapper {

    private final ReservationTimeMapper timeMapper;
    private final ThemeMapper themeMapper;

    public ReservationResponse mapToResponse(
            Reservation reservation
    ) {
        ReservationTimeResponse time = timeMapper.mapToResponse(reservation.getTime());
        ThemeResponse theme = themeMapper.mapToResponse(reservation.getTheme());

        return new ReservationResponse(
                reservation.getId(),
                reservation.getName(),
                reservation.getDate(),
                time,
                theme
        );
    }
}
