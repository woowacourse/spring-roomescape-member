package roomescape.acceptancetest;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestExecutionListeners;
import roomescape.support.testexecutionlistener.TestDatabaseInitializer;
import roomescape.support.testexecutionlistener.TestPortInitializer;
import roomescape.support.time.FixedTimeConfig;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestExecutionListeners(
        listeners = {
                TestPortInitializer.class,
                TestDatabaseInitializer.class
        },
        mergeMode = TestExecutionListeners.MergeMode.MERGE_WITH_DEFAULTS
)
@Import(FixedTimeConfig.class)
public @interface RoomecapeAcceptanceTest {
}
