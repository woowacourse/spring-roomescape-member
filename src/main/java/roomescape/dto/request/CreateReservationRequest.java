package roomescape.dto.request;

import java.time.LocalDate;

public record CreateReservationRequest(
        LocalDate date,
        Long timeId,
        Long themeId
) {

    // TODO: spring validation or RequestBody 없이 파라미터로 바로 입력
    public CreateReservationRequest {
        if (date == null) {
            throw new IllegalArgumentException("날짜는 필수로 입력해야 합니다.");
        }
        if (timeId == null) {
            throw new IllegalArgumentException("시간은 필수로 입력해야 합니다.");
        }
        if (themeId == null) {
            throw new IllegalArgumentException("테마는 필수로 선택해야 합니다.");
        }
    }
}
