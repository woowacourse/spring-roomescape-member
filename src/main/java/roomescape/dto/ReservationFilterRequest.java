package roomescape.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;

public record ReservationFilterRequest(
        Long themeId,
        Long memberId,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
        LocalDate dateFrom,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
        LocalDate dateTo
) {
}
