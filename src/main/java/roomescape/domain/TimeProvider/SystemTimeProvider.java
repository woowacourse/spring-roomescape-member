package roomescape.domain.TimeProvider;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

@Component
public class SystemTimeProvider implements TimeProvider {
    @Override
    public LocalDateTime getCurrentDateTime() {
        return LocalDateTime.now();
    }
}
