package roomescape.admin;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import java.util.HashMap;
import java.util.Map;

import static roomescape.fixture.MemberFixture.adminFixture;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class AdminTest {

    private String token;

    @LocalServerPort
    int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @BeforeEach
    void adminLogIn() {
        Map<String, String> params = new HashMap<>();
        params.put("email", adminFixture.getEmail());
        params.put("password", adminFixture.getPassword());

        String cookie = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/login")
                .then().log().all().extract().header("Set-Cookie");
        token = cookie.substring("token=".length(), cookie.indexOf(';'));
    }

    @DisplayName("어드민 메인 페이지 테스트")
    @Test
    void adminMainPage() {
        RestAssured.given().log().all()
                .cookie("token", token)
                .when().get("/admin")
                .then().log().all()
                .statusCode(200);
    }

    @DisplayName("예약 어드민 페이지 테스트")
    @Test
    void readReservations() {
        RestAssured.given().log().all()
                .cookie("token", token)
                .when().get("/admin/reservation")
                .then().log().all()
                .statusCode(200);
    }

    @DisplayName("시간 추가 페이지 테스트")
    @Test
    void timePage() {
        RestAssured.given().log().all()
                .cookie("token", token)
                .when().get("/admin/time")
                .then().log().all()
                .statusCode(200);
    }
}
