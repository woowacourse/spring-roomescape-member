package roomescape.dto.reservation;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Objects;
import java.util.regex.Pattern;

import roomescape.application.dto.ReservationCreationRequest;

public record ReservationRequest(String name, String date, Long timeId) {
    private static final Pattern DATE_FORMAT = Pattern.compile("^\\d{4}-\\d{2}-\\d{2}$");

    public ReservationRequest {
        Objects.requireNonNull(name, "예약자명은 필수입니다.");
        Objects.requireNonNull(date, "예약날짜는 필수입니다.");
        Objects.requireNonNull(timeId, "예약시간은 필수입니다.");

        if (!DATE_FORMAT.matcher(date).matches()) {
            throw new IllegalArgumentException("yyyy-MM-dd 형식이어야 합니다.");
        }

        try {
            LocalDate.parse(date);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("잘못된 날짜입니다.");
        }
    }

    public ReservationCreationRequest toReservationCreationRequest() {
        return new ReservationCreationRequest(name, LocalDate.parse(date), timeId);
    }
}
