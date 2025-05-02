package roomescape.presentation.dto.request;

import java.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;

public record ThemeRankingRequest (
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    LocalDate startDate,

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    LocalDate endDate,

    Integer limit
){
}
