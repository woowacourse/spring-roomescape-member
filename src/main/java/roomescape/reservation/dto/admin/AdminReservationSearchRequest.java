package roomescape.reservation.dto.admin;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;
import roomescape.common.exception.InvalidDateException;
import roomescape.common.exception.InvalidIdException;

public record AdminReservationSearchRequest(
        Long memberId,
        Long themeId,
        @JsonFormat(pattern = "yyyy-MM-dd") LocalDate dateFrom,
        @JsonFormat(pattern = "yyyy-MM-dd") LocalDate dateTo
) {
    public AdminReservationSearchRequest {
        if (dateFrom == null || dateTo == null) {
            throw new InvalidDateException("날짜를 선택해주세요");
        }
        if (memberId == null) {
            throw new InvalidIdException("멤버 아이디를 선택해주세요");
        }
        if (themeId == null) {
            throw new InvalidIdException("테마 아이디를 선택해주세요");
        }
    }
}
