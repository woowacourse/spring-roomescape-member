package roomescape.acceptancetest;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.TestExecutionListeners;
import roomescape.DatabaseInitializeTest;
import roomescape.testexecutionlistener.TestDatabaseInitializer;
import roomescape.testexecutionlistener.TestPortInitializer;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@DatabaseInitializeTest
@TestExecutionListeners(
        listeners = {
                TestPortInitializer.class,
        },
        mergeMode = TestExecutionListeners.MergeMode.MERGE_WITH_DEFAULTS
)
public @interface RoomecapeAcceptanceTest {
}
