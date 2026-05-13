package roomescape.domain.reservation.error.exception;

import java.util.List;
import org.springframework.http.HttpStatus;
import roomescape.domain.reservation.error.type.ReservationErrorType;
import roomescape.global.error.exception.dto.FieldErrorResponseDto;

public class ReservationNotFoundException extends RuntimeException {

    private final HttpStatus status;
    private final List<FieldErrorResponseDto> fieldErrors;

    public ReservationNotFoundException(ReservationErrorType errorType, List<FieldErrorResponseDto> fieldErrors) {
        super(errorType.message());
        this.status = errorType.status();
        this.fieldErrors = fieldErrors;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public List<FieldErrorResponseDto> getFieldErrors() {
        return fieldErrors;
    }
}
