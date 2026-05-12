package roomescape.global.exception;

import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Component;
import roomescape.global.exception.base.BusinessException;
import roomescape.global.exception.common.InvalidFormatException;

@Component
public class HttpMessageNotReadableExceptionMapper {

    public BusinessException resolve(
            HttpMessageNotReadableException e
    ) {
        Throwable cause = e;

        while (cause != null) {
            if (cause instanceof BusinessException be) {
                return be;
            }

            cause = cause.getCause();
        }

        return new InvalidFormatException();
    }
}
