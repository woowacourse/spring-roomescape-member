package roomescape.time.controller.dto;

import java.time.LocalTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;

public record TimeSaveRequestDto(
    @NotNull @JsonFormat(pattern = "HH:mm") LocalTime startAt,
    @NotNull @JsonFormat(pattern = "HH:mm") LocalTime endAt
) {}
