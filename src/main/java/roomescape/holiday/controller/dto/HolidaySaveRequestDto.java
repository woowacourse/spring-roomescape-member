package roomescape.holiday.controller.dto;

import roomescape.holiday.service.dto.HolidaySaveServiceDto;

import java.time.LocalDate;

public record HolidaySaveRequestDto(Long id, LocalDate date) {
    public HolidaySaveServiceDto toServiceDto() {
        return new HolidaySaveServiceDto(date);
    }
}
