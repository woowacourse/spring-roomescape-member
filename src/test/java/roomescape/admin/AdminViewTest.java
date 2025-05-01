package roomescape.admin;

import io.restassured.RestAssured;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;


@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class AdminViewTest {

    private final int port;

    public AdminViewTest(
            @LocalServerPort final int port
    ){
        this.port = port;
    }

    @DisplayName("/admin으로 요청이 들어오면 어드민 페이지를 응답한다.")
    @Test
    void admin() {
        RestAssured.given().port(port).log().all()
                .when().get("/admin")
                .then().log().all()
                .statusCode(200);
    }

    @DisplayName("/admin/reservation으로 요청이 들어오면 예약 페이지를 응답한다.")
    @Test
    void adminReservation() {
        RestAssured.given().port(port).log().all()
                .when().get("/admin/reservation")
                .then().log().all()
                .statusCode(200);
    }
  
    @DisplayName("/admin/time으로 요청이 들어오면 시간 설정 페이지를 응답한다.")
    @Test
    void adminTime() {
        RestAssured.given().port(port).log().all()
                .when().get("/admin/time")
                .then().log().all()
                .statusCode(200);
    }

    @DisplayName("/admin/theme으로 요청이 들어오면 테마 설정 페이지를 응답한다.")
    @Test
    void adminTheme() {
        RestAssured.given().port(port).log().all()
                .when().get("/admin/theme")
                .then().log().all()
                .statusCode(200);
    }
}
