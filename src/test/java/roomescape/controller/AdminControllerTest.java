package roomescape.controller;


import io.restassured.RestAssured;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = "/truncate.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
public class AdminControllerTest {

    @LocalServerPort
    private int port;

    @Test
    @DisplayName("/ 로 get 요청을 보내면 응답 코드는 200이다.")
    void getMainPage() {
        RestAssured.given().log().all()
                .port(port)
                .when().get("/")
                .then().log().all()
                .statusCode(200);
    }

    @Test
    @DisplayName("/admin 으로 get 요청을 보내면 응답 코드는 200이다.")
    void getAdminPage() {
        RestAssured.given().log().all()
                .port(port)
                .when().get("/admin")
                .then().log().all()
                .statusCode(200);
    }

    @Test
    @DisplayName("/admin/reservation 으로 get 요청을 보내면 응답 코드는 200이다.")
    void getAdminReservationPage() {
        RestAssured.given().log().all()
                .port(port)
                .when().get("/admin/reservation")
                .then().log().all()
                .statusCode(200);
    }
}
