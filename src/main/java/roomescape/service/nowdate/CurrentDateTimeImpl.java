package roomescape.service.nowdate;

import java.time.LocalDateTime;
import org.springframework.stereotype.Component;

@Component
public class CurrentDateTimeImpl implements CurrentDateTime {

    @Override
    public LocalDateTime get() {
        return LocalDateTime.now();
    }
}
