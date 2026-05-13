package roomescape.dto.request;

import java.time.LocalDate;

public record UserReservationUpdateRequest(LocalDate date, Long timeId) {
}
