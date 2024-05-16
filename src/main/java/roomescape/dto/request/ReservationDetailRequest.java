package roomescape.dto.request;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

public record ReservationDetailRequest(Long themeId,
                                       Long memberId,
                                       @JsonFormat(pattern = "yyyy-MM-dd") LocalDate dateFrom,
                                       @JsonFormat(pattern = "yyyy-MM-dd") LocalDate dateTo) {
}
