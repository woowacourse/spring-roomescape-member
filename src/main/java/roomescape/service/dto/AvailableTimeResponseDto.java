package roomescape.service.dto;

import roomescape.domain.ReservationTimeStatus;

public class AvailableTimeResponseDto {

    private final ReservationTimeResponseDto timeResponseDto;
    private final boolean booked;

    public AvailableTimeResponseDto(ReservationTimeResponseDto timeResponseDto, boolean booked) {
        this.timeResponseDto = timeResponseDto;
        this.booked = booked;
    }

    public AvailableTimeResponseDto(ReservationTimeStatus reservationTimeStatus) {
        this(new ReservationTimeResponseDto(reservationTimeStatus.getReservationTime()),
                reservationTimeStatus.getReservationStatus().getStatus()
        );
    }

    public ReservationTimeResponseDto getTimeResponseDto() {
        return timeResponseDto;
    }

    public boolean isBooked() {
        return booked;
    }
}
