package roomescape.service.exception;

import roomescape.exception.BadRequestException;

public class RowsLimitException extends BadRequestException {

    public RowsLimitException(String message) {
        super(message);
    }
}
