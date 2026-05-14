package roomescape.exception;

import org.springframework.http.HttpStatus;

public class InvalidOwnershipException extends RoomescapeException {
    public InvalidOwnershipException() {
        super("본인의 예약만 제어할 수 있습니다.", HttpStatus.FORBIDDEN);
    }
}
