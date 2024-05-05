package roomescape.dto;

import java.time.LocalDate;

public record BookableTimesRequest(LocalDate date, Long themeId) {

}
