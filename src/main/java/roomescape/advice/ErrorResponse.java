package roomescape.advice;

import roomescape.global.BusinessException;

public record ErrorResponse(String message) {

    public static ErrorResponse of(BusinessException e) {
        return new ErrorResponse(e.getMessage());
    }
}
