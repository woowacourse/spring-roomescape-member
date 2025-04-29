package roomescape.common.util;

import java.time.LocalDateTime;
import org.springframework.stereotype.Component;

@Component
public class SystemDateTime implements DateTime {

    @Override
    public LocalDateTime now() {
        return LocalDateTime.now();
    }
}
