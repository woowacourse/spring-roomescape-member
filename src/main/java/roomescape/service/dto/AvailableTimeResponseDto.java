package roomescape.service.dto;

import roomescape.domain.ReservationTime;

public class AvailableTimeResponseDto {

    private final ReservationTimeResponseDto timeResponseDto;
    private final boolean isBooked;

    public AvailableTimeResponseDto(ReservationTime reservationTime, boolean isBooked) {
        this.timeResponseDto = new ReservationTimeResponseDto(reservationTime);
        this.isBooked = isBooked;
    }

    public ReservationTimeResponseDto getTimeResponseDto() {
        return timeResponseDto;
    }

    public boolean isBooked() {
        return isBooked;
    }
}
