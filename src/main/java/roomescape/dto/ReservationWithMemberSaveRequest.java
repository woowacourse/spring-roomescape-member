package roomescape.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record ReservationWithMemberSaveRequest(
        @NotNull(message = "멤버 아이디가 비어 있습니다.")
        Long memberId,

        @NotNull(message = "예약 날짜가 비어 있습니다.")
        @JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
        LocalDate date,

        @NotNull(message = "예약 시간 아이디가 비어 있습니다.")
        Long timeId,

        @NotNull(message = "예약 테마 아이디가 비어 있습니다.")
        Long themeId
) {
}
