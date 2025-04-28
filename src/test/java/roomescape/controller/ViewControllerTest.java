package roomescape.controller;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import io.restassured.RestAssured;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class ViewControllerTest {
//    @DisplayName("/ 페이지 연결 테스트(웰컴 페이지)")
//    @Test
//    void welcomePage() {
//        RestAssured.given().log().all()
//                .when().get("/")
//                .then().log().all()
//                .statusCode(200);
//    }

    @DisplayName("/admin 페이지 연결 테스트")
    @Test
    void adminPage() {
        RestAssured.given().log().all()
                .when().get("/admin")
                .then().log().all()
                .statusCode(200);
    }

    @DisplayName("/admin/reservation 페이지 연결 테스트")
    @Test
    void adminReservationPage() {
        RestAssured.given().log().all()
                .when().get("admin/reservation")
                .then().log().all()
                .statusCode(200);
    }

    @DisplayName("/admin/time 페이지 연결 테스트")
    @Test
    void adminTimePage() throws URISyntaxException, IOException {
        // [요구사항] RestAssured 를 사용하지 않고 테스트 코드 작성해보기.
        HttpURLConnection connection = (HttpURLConnection) new URI("http://localhost:8080/admin/time").toURL().openConnection();
        int responseCode = connection.getResponseCode();

        Assertions.assertThat(responseCode).isEqualTo(200);
    }
}
