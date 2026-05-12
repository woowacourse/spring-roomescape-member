package roomescape.testexecutionlistener;

import io.restassured.RestAssured;
import org.springframework.boot.web.context.WebServerApplicationContext;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.support.AbstractTestExecutionListener;

public class TestPortInitializer extends AbstractTestExecutionListener {

    @Override
    public void beforeTestClass(TestContext testContext) {
        WebServerApplicationContext applicationContext =
                (WebServerApplicationContext) testContext.getApplicationContext();

        RestAssured.port = applicationContext.getWebServer().getPort();
    }

}
