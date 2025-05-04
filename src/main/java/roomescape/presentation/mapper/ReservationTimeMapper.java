package roomescape.presentation.mapper;

import roomescape.business.ReservationTime;
import roomescape.presentation.dto.ReservationTimeResponseDto;

public final class ReservationTimeMapper {

    private ReservationTimeMapper() {
    }

    public static ReservationTimeResponseDto toResponse(ReservationTime reservationTime) {
        return new ReservationTimeResponseDto(
                reservationTime.getId(),
                reservationTime.getStartAt()
        );
    }
}
