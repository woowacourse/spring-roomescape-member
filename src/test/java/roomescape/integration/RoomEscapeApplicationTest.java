package roomescape.integration;

import static org.hamcrest.Matchers.is;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class RoomEscapeApplicationTest {

    @LocalServerPort
    private int port;

    private RequestSpecification spec;

    @BeforeEach
    void setUp() {
        spec = new RequestSpecBuilder()
                .setBaseUri("http://localhost")
                .setPort(port)
                .build();

        RestAssured.requestSpecification = spec;
    }

    @Test
    @DisplayName("인기 테마 목록을 조회한다.")
    void readAllThemes() {
        RestAssured.given().log().all()
                .when().get("/themes/rankings")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(10))
                .body("[0].name", is("우테코 레벨1 탈출"))
                .body("[1].name", is("우테코 레벨2 탈출"))
                .body("[2].name", is("우테코 레벨3 탈출"))
                .body("[3].name", is("우테코 레벨4 탈출"))
                .body("[4].name", is("우테코 레벨5 탈출"))
                .body("[5].name", is("우테코 레벨6 탈출"))
                .body("[6].name", is("우테코 레벨7 탈출"))
                .body("[7].name", is("우테코 레벨8 탈출"))
                .body("[8].name", is("우테코 레벨9 탈출"))
                .body("[9].name", is("우테코 레벨10 탈출"));
    }
}
