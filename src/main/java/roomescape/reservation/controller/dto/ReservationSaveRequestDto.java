package roomescape.reservation.controller.dto;

import roomescape.reservation.service.dto.ReservationSaveServiceDto;

public record ReservationSaveRequestDto(String name, String date, Long timeId) {

    public ReservationSaveServiceDto toServiceDto() {
        return new ReservationSaveServiceDto(name, date, timeId);
    }
}
