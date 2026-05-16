package roomescape.global.exception.exception;

import lombok.Getter;
import roomescape.global.exception.GlobalErrorCode;

@Getter
public class UnexpectedUpdateCountException extends RuntimeException {
    public UnexpectedUpdateCountException(int updateRowCount) {
        super(GlobalErrorCode.UNEXPECTED_UPDATE_COUNT.getMessage() + " 업데이트된 행 수: " + updateRowCount);
    }
}
