package roomescape.support.extension;

import static org.mockito.Mockito.when;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Objects;
import java.util.regex.Pattern;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import roomescape.support.annotation.FixedClock;

public class MockClockExtension implements BeforeEachCallback {
    private static final Pattern DATE_PATTERN = Pattern.compile("\\d{4}-\\d{2}-\\d{2}");

    @Override
    public void beforeEach(ExtensionContext context) {
        Clock clockBean = SpringExtension.getApplicationContext(context).getBean(Clock.class);
        FixedClock fixedClockAnnotation = context.getRequiredTestClass().getDeclaredAnnotation(FixedClock.class);
        Objects.requireNonNull(fixedClockAnnotation, "@FixedClock 어노테이션이 필요합니다");
        String date = getDate(fixedClockAnnotation);
        when(clockBean.instant()).thenReturn(fixClock(date).instant());
    }

    private String getDate(FixedClock fixedClockAnnotation) {
        String date = fixedClockAnnotation.date();
        if (!DATE_PATTERN.matcher(date).matches()) {
            throw new IllegalArgumentException("yyyy-MM-dd의 날짜 형식이어야 합니다.");
        }
        return date;
    }

    private Clock fixClock(String date) {
        return Clock.fixed(Instant.parse(date + "T00:00:00Z"), ZoneId.of("UTC"));
    }
}
