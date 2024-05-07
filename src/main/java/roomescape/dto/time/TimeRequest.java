package roomescape.dto.time;

import io.micrometer.common.util.StringUtils;
import org.springframework.format.annotation.DateTimeFormat;
import roomescape.domain.time.Time;
import roomescape.global.exception.error.ErrorType;
import roomescape.global.exception.model.ValidateException;

import java.time.LocalTime;

public record TimeRequest(
        @DateTimeFormat(pattern = "kk:mm")
        LocalTime startAt
) {

    public TimeRequest {
        if (StringUtils.isBlank(startAt.toString())) {
            throw new ValidateException(ErrorType.REQUEST_DATA_BLANK,
                    String.format("공백 또는 null이 포함된 요청입니다. [values: %s]", this));
        }
    }

    public Time toTime() {
        return new Time(this.startAt);
    }
}
