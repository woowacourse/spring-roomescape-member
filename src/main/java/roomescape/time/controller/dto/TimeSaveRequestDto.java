package roomescape.time.controller.dto;

import java.time.LocalTime;

import com.fasterxml.jackson.annotation.JsonFormat;

public record TimeSaveRequestDto(
    @JsonFormat(pattern = "HH:mm") LocalTime startAt,
    @JsonFormat(pattern = "HH:mm") LocalTime endAt
) {}
