package roomescape.support.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.time.Clock;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.SpyBean;
import roomescape.support.extension.MockClockExtension;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@SpyBean(Clock.class)
@ExtendWith(MockClockExtension.class)
public @interface FixedClock {
    String date();
}
