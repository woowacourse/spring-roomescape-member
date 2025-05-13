package roomescape.integration;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.jdbc.Sql;


@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@Sql({"/schema.sql", "/data.sql"})
public class ViewIntegrationTest {

    @LocalServerPort
    int port;

    private String token;

    @BeforeEach
    void beforeEach() {
        RestAssured.port = this.port;

        final Map<String, String> credentials = new HashMap<>();
        credentials.put("email", "admin@email.com");
        credentials.put("password", "password");

        token = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(credentials)
                .when().post("/login")
                .then().log().all()
                .statusCode(200)
                .extract().cookie("token");
    }

    @ParameterizedTest
    @ValueSource(strings = {"/admin", "/admin/reservation", "/admin/time", "/admin/theme"})
    void getAdminPageTest(final String uri) {
        RestAssured.given().log().all()
                .cookie("token", token)
                .when().get(uri)
                .then().log().all()
                .statusCode(200);
    }

    @ParameterizedTest
    @ValueSource(strings = {"/reservation", "/"})
    void getUserPageTest(final String uri) {
        RestAssured.given().log().all()
                .when().get(uri)
                .then().log().all()
                .statusCode(200);
    }
}
