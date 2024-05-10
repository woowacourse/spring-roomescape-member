package roomescape.service.dto.request;

import java.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;

public record ReservationConditionRequest(Long themeId,
                                          Long memberId,
                                          @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                                          LocalDate dateFrom,
                                          @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                                          LocalDate dateTo) {
}
