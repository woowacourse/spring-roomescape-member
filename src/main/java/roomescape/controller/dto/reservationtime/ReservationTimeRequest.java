package roomescape.controller.dto.reservationtime;

import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import roomescape.global.exception.InvalidReservationTimeException;
import roomescape.service.dto.CreateReservationTimeCommand;

public record ReservationTimeRequest(
        String startAt
) {

    public CreateReservationTimeCommand toCommand() {
        return new CreateReservationTimeCommand(parseStartAt());
    }

    private LocalTime parseStartAt() {
        if (startAt == null || startAt.isBlank()) {
            throw new InvalidReservationTimeException("예약 시간은 필수입니다.");
        }

        try {
            return LocalTime.parse(startAt);
        } catch (DateTimeParseException e) {
            throw new InvalidReservationTimeException("예약 시간 형식은 HH:mm 이어야 합니다.");
        }
    }
}
