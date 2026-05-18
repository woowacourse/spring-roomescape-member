package roomescape.exception;

import org.springframework.http.HttpStatus;

public class DataInconsistencyException extends RoomeScapeServerException {
    public DataInconsistencyException(String message) {
        super(message, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
