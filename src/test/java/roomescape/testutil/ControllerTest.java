package roomescape.testutil;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.ActiveProfiles;
import roomescape.auth.TokenProvider;
import roomescape.common.DateTimeFormatConfiguration;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@ActiveProfiles("test")
@WebMvcTest({
        DateTimeFormatConfiguration.class,
        TokenProvider.class
})
public @interface ControllerTest {
}
