package roomescape.dto.request;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

public record ReservationMemberCreateRequest(@JsonFormat(pattern = "yyyy-MM-dd") LocalDate date,
                                             Long timeId,
                                             Long themeId) {
}
