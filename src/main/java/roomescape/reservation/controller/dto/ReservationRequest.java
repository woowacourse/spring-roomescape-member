package roomescape.reservation.controller.dto;

import java.time.LocalDate;
import roomescape.reservation.service.ReservationCommand;

import java.util.regex.Pattern;

public record ReservationRequest(String name, LocalDate date, Long timeId) {

    public ReservationRequest {
        validateEmptyName(name);
    }

    public ReservationCommand toCommand() {
        return new ReservationCommand(
                name,
                date,
                timeId
        );
    }

    private void validateEmptyName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("이름은 비어있을 수 없습니다.");
        }
    }
}
