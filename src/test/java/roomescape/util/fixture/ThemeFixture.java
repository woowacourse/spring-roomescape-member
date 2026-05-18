package roomescape.util.fixture;

import java.util.concurrent.atomic.AtomicLong;
import roomescape.theme.domain.Theme;

public class ThemeFixture {

    private static final AtomicLong idSequence = new AtomicLong(1L);

    public static Theme createDefault() {
        return new Theme(idSequence.getAndIncrement(), "default", "default", "/image/...");
    }

    public static Theme createByIdAndName(Long id, String name) {
        return new Theme(id, name, "default", "/image/...");
    }
}
