package roomescape.reservation.controller.dto;

import java.time.LocalDate;

import roomescape.reservation.service.dto.ReservationSaveServiceDto;

public record ReservationSaveRequestDto(String name, LocalDate date, Long timeId) {

    public ReservationSaveServiceDto toServiceDto() {
        return new ReservationSaveServiceDto(name, date, timeId);
    }
}
