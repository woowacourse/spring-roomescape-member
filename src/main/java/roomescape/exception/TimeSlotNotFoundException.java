package roomescape.exception;

import org.springframework.http.HttpStatus;

public class TimeSlotNotFoundException extends RoomescapeException {
    public TimeSlotNotFoundException(long id) {
        super("해당 식별자의 시간을 찾을 수 없습니다. id: " + id, HttpStatus.NOT_FOUND);
    }
}
