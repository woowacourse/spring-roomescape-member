package roomescape.common.exception.handler;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.springframework.stereotype.Component;

import java.time.LocalTime;

@Component
public class LocalTimeHandler implements FormatHandler {
    @Override
    public boolean isSupport(InvalidFormatException e) {
        return e.getTargetType() == LocalTime.class;
    }

    @Override
    public String handle(InvalidFormatException formatException) {
        return "시간 형식은 TT:mm 으로 입력해 주세요. (ex. 10:00)";
    }
}
