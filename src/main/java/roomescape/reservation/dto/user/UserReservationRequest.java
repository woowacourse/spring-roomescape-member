package roomescape.reservation.dto.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;
import roomescape.common.exception.InvalidDateException;
import roomescape.common.exception.InvalidIdException;

public record UserReservationRequest(
        @JsonFormat(pattern = "yyyy-MM-dd") LocalDate date,
        Long timeId,
        Long themeId
) {
    public UserReservationRequest {
        if (date == null) {
            throw new InvalidDateException("날짜를 입력해주세요");
        }
        if (date.isBefore(LocalDate.now())) {
            throw new InvalidDateException("이미 지난 날짜로는 예약할 수 없습니다.");
        }
        if (timeId == null) {
            throw new InvalidIdException("시간 아이디를 입력해주세요");
        }
        if (themeId == null) {
            throw new InvalidIdException("테마 아이디를 입력해주세요");
        }
    }
}
