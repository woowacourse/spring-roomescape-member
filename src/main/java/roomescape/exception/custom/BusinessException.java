package roomescape.exception.custom;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import roomescape.exception.code.ErrorCode;

@Getter
@RequiredArgsConstructor
public class BusinessException extends RuntimeException {
    private final ErrorCode errorCode;
}
