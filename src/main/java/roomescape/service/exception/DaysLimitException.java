package roomescape.service.exception;

import roomescape.exception.BadRequestException;

public class DaysLimitException extends BadRequestException {
    
    public DaysLimitException(String message) {
        super(message);
    }
}
