package roomescape.domain.theme;

import java.time.LocalDate;

public record PopularThemeCondition(LocalDate startDate, LocalDate endDate, long size) {
}
