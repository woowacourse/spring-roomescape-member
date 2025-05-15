package roomescape.dto.request;

import java.time.LocalDate;
import com.fasterxml.jackson.annotation.JsonFormat;

public record CreateReservationRequest(
        Long memberId,
        @JsonFormat(pattern = "yyyy-MM-dd") LocalDate date,
        Long timeId,
        Long themeId
) {
    // ? static 으로 선언하면, 가변 setter 로 착각해서 반환 값을 받지 않는 실수를 방지할 수 있을 것 같다고 판단했다.
    public static CreateReservationRequest setMember(CreateReservationRequest request, Long memberId) {
        return new CreateReservationRequest(
                memberId,
                request.date,
                request.timeId,
                request.themeId
        );
    }
}
