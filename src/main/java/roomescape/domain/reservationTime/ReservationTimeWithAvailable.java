package roomescape.domain.reservationTime;


import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import roomescape.exception.InvalidRequestValueException;

public record ReservationTimeWithAvailable(long id, LocalTime startAt, boolean isAvailable) {
    private static final String INVALID_START_TIME_NULL = "시작 시간은 필수입니다.";
    private static final String INVALID_START_TIME_FORMAT = "유효하지 않은 시간입니다.";

    public ReservationTimeWithAvailable(long id, String startAt, boolean isAvailable) {
        this(id, validateLocalDate(startAt), isAvailable);
    }

    public static LocalTime validateLocalDate(String startAt) {
        if(startAt == null || startAt.isBlank()) {
            throw new InvalidRequestValueException(INVALID_START_TIME_NULL);
        }

        try {
            return LocalTime.parse(startAt);
        } catch (DateTimeParseException e) {
            throw new InvalidRequestValueException(INVALID_START_TIME_FORMAT);

        }
    }
}
