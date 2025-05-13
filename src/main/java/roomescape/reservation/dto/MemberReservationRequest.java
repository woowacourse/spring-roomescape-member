package roomescape.reservation.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;

public record MemberReservationRequest(@JsonFormat(pattern = "yyyy-MM-dd") LocalDate date,
                                       Long timeId,
                                       Long themeId) {

    public MemberReservationRequest {
        if (date == null) {
            throw new IllegalArgumentException("[ERROR] 날짜를 입력해주세요.");
        }
        if (date.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("[ERROR] 지난 날짜로는 예약할 수 없습니다.");
        }
        if (timeId == null) {
            throw new IllegalArgumentException("[ERROR] 시간 id를 입력해주세요.");
        }
        if (themeId == null) {
            throw new IllegalArgumentException("[ERROR] 테마 id를 입력해주세요.");
        }
    }
}
