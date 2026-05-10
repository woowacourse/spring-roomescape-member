package roomescape.controller.dto;

import java.time.LocalDate;

public record GetAvailableTimesRequest(
        Long themeId,
        LocalDate date,
        Boolean available
) {
    public static GetAvailableTimesRequest of(Long themeId, LocalDate date,  Boolean available) {
        return new GetAvailableTimesRequest(themeId, date, available);
    }
}
