package roomescape.reservationTime.dto.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;
import roomescape.common.exception.InvalidDateException;
import roomescape.common.exception.InvalidIdException;

public record AvailableReservationTimeRequest(@JsonFormat(pattern = "yyyy-MM-dd") LocalDate date, Long themeId) {

    public AvailableReservationTimeRequest {
        if (date == null) {
            throw new InvalidDateException("날짜를 입력해주세요");
        }
        if (date.isBefore(LocalDate.now())) {
            throw new InvalidDateException("이전 날짜 값은 입력할 수 없습니다");
        }
        if (themeId == null) {
            throw new InvalidIdException("테마 아이디를 입력해주세요");
        }
    }
}
