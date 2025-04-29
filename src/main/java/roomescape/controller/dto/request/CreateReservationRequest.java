package roomescape.controller.dto.request;

import java.time.LocalDate;

public record CreateReservationRequest(String name, LocalDate date, long timeId) {
}
