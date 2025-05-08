package roomescape.service;

import java.time.LocalDateTime;
import roomescape.service.nowdate.CurrentDateTime;

public class TestTime implements CurrentDateTime {

    @Override
    public LocalDateTime get() {
        return LocalDateTime.of(2025, 1, 1, 10, 30);
    }
}
