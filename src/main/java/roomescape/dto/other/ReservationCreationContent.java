package roomescape.dto.other;

import java.time.LocalDate;
import roomescape.dto.request.ReservationCreationAdminRequest;
import roomescape.dto.request.ReservationCreationRequest;

public record ReservationCreationContent(
        Long memberId,
        LocalDate date,
        Long timeId,
        Long themeId
) {

    public ReservationCreationContent(ReservationCreationAdminRequest request) {
        this(request.memberId(), request.date(), request.timeId(), request.themeId());
    }

    public ReservationCreationContent(Long memberId, ReservationCreationRequest request) {
        this(memberId, request.date(), request.timeId(), request.themeId());
    }
}
