package roomescape.global.exception.advice;

import roomescape.global.exception.base.BusinessException;

public record ErrorResponse(String message) {

    public static ErrorResponse of(BusinessException e) {
        return new ErrorResponse(e.getMessage());
    }
}
