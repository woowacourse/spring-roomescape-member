package roomescape.dto.request;

import java.time.LocalDate;

// TODO: 이름 고려해보기
public record CreateReservationByAdminRequest(
        Long memberId,
        Long timeId,
        Long themeId,
        LocalDate date
) {
}
