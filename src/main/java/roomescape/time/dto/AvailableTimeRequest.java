package roomescape.time.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;

public record AvailableTimeRequest(@JsonFormat(pattern = "yyyy-MM-dd") LocalDate date, Long themeId) {

    public AvailableTimeRequest {
        if (date == null) {
            throw new IllegalArgumentException("[ERROR] 날짜를 입력해주세요.");
        }
        if (date.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("[ERROR] 이전 날짜 값은 입력할 수 없습니다.");
        }
        if (themeId == null) {
            throw new IllegalArgumentException("[ERROR] 테마 id를 입력해주세요.");
        }
    }
}
