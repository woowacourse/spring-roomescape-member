package roomescape.domain.TimeProvider;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

@Component
public interface TimeProvider {
    LocalDateTime getCurrentDateTime();
}
