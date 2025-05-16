package roomescape.domain.reservationtime.dto.request;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import roomescape.domain.reservationtime.model.ReservationTime;

public record ReservationTimeRequestDto(String startAt) {

    public ReservationTimeRequestDto {
        validateStartAt(startAt);
    }

    public ReservationTime toReservationTime() {
        return new ReservationTime(LocalTime.parse(startAt));
    }

    private void validateStartAt(String startAt) {
        if (startAt == null || startAt.isEmpty()) {
            throw new IllegalArgumentException("잘못된 시간입니다.");
        }
        validateParse(startAt);
    }

    private void validateParse(String startAt) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        try {
            LocalTime.parse(startAt, dateTimeFormatter);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("잘못된 시간 형식입니다.");
        }
    }
}
