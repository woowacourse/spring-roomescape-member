package roomescape.domain.global.exception.custom;

import roomescape.domain.global.exception.error.ErrorCode;

public interface BaseException {

    ErrorCode getErrorCode();
}
