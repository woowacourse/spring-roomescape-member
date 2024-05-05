package roomescape.service.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonCreator.Mode;
import roomescape.domain.ReservationTimeStatus;

public class AvailableTimeResponseDto {

    private final ReservationTimeResponseDto timeResponseDto;
    private final boolean booked;

    @JsonCreator(mode = Mode.PROPERTIES)
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
