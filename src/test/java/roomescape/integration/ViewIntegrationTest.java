package roomescape.integration;

import io.restassured.RestAssured;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT,
        properties = {
                "spring.sql.init.mode=always",
                "spring.sql.init.data-locations=classpath:/test-data.sql"
        })
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ViewIntegrationTest {

    @ParameterizedTest
    @ValueSource(strings = {"/admin", "/admin/reservation", "/admin/time", "/admin/theme", "/reservation", "/"})
    void getPageTest(String uri) {
        RestAssured.given().log().all()
                .when().get(uri)
                .then().log().all()
                .statusCode(200);
    }
}
