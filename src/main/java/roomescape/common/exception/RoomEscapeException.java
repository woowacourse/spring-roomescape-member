package roomescape.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class RoomEscapeException extends RuntimeException{

    private final HttpStatus status;

    protected RoomEscapeException(ExceptionInformation info) {
        super(info.getMessage());
        this.status = info.getHttpStatus();
    }

}
