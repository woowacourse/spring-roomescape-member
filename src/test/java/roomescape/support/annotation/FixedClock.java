package roomescape.support.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.time.Clock;
import org.springframework.boot.test.mock.mockito.SpyBean;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@SpyBean(Clock.class)
public @interface FixedClock {
    String date();
}
