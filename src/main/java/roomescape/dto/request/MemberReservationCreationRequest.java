package roomescape.dto.request;

import java.time.LocalDate;

public record MemberReservationCreationRequest(LocalDate date, Long themeId, Long timeId) {
    
}
