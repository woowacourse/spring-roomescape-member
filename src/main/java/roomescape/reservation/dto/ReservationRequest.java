package roomescape.reservation.dto;

import java.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;

public record ReservationRequest(String name, @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date, Long timeId) {

    public ReservationRequest {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("[ERROR] 이름을 입력해주세요");
        }
        if (date == null) {
            throw new IllegalArgumentException("[ERROR] 날짜를 입력해주세요");
        }
        if (timeId == null) {
            throw new IllegalArgumentException("[ERROR] 시간 id를 입력해주세요");
        }
    }
}
