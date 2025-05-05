package roomescape.common.exception;

import org.springframework.http.HttpStatus;

public class ResourceInUseException extends CustomException {
    public ResourceInUseException(String message) {
        super(message, HttpStatus.UNPROCESSABLE_ENTITY);
    }
}
