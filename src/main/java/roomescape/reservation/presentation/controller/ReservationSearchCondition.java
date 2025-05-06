package roomescape.reservation.presentation.controller;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;

public record ReservationSearchCondition(

        String memberName,

        Long themeId,

        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate dateFrom,

        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate dateTo
) {
    public boolean isEmpty() {
        return memberName == null && themeId == null && dateFrom == null && dateTo == null;
    }
}
