package roomescape.reservation.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;

public record ReservationCreateRequest(
        String name,
        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate date,
        long timeId,
        long themeId
) {
    public ReservationCreateRequest {
        if (name == null) {
            throw new IllegalArgumentException("이름은 반드시 입력해야합니다.");
        }
    }
}
