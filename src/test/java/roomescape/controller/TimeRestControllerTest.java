package roomescape.controller;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.util.Map;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TimeRestControllerTest {

    @LocalServerPort
    private Integer port;

    @BeforeEach
    void setPort() {
        RestAssured.port = port;
    }

    @ParameterizedTest
    @EmptySource
    void createTime_invalid_time_bad_request(String input) {
        Map<String, String> params = Map.of(
                "startAt", input
        );
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/times")
                .then().log().all()
                .statusCode(HttpStatus.SC_BAD_REQUEST);
    }
}
