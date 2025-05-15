package roomescape.controller;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;

@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
class ReservationTimeControllerTest {

    @Nested
    class FailureTest {

        String jwtToken;

        @BeforeEach
        void setUp() {
            String email = "jeffrey@gmail.com";
            String password = "1234!@#$";

            Response loginResponse = RestAssured.given()
                    .contentType(ContentType.JSON)
                    .body(Map.of("email", email, "password", password))
                    .when()
                    .post("/login");

            jwtToken = loginResponse.getCookie("token");
        }

        @DisplayName("시간 포맷이 유효하지 않아 예외가 발생한다")
        @Test
        void reservationTimeAddTest() {
            Map<String, String> request = Map.of("startAt", "12;00");
            RestAssured.given().log().all()
                    .contentType(ContentType.JSON)
                    .cookie("token", jwtToken)
                    .body(request)
                    .when()
                    .post("/times")
                    .then().log().all()
                    .statusCode(400);
        }

        @DisplayName("예약과 연관된 시간을 삭제하여 예외가 발생한다")
        @Test
        void reservationTimeRemoveTest() {
            // given
            RestAssured.given().log().all()
                    .contentType(ContentType.JSON)
                    .cookie("token", jwtToken)
                    .when()
                    .delete("/times/{id}", 1L)
                    .then().log().all()
                    .statusCode(400);
        }
    }

    @Nested
    class SuccessTest {

        String jwtToken;

        @BeforeEach
        void setUp() {
            String email = "jeffrey@gmail.com";
            String password = "1234!@#$";

            Response loginResponse = RestAssured.given()
                    .contentType(ContentType.JSON)
                    .body(Map.of("email", email, "password", password))
                    .when()
                    .post("/login");

            jwtToken = loginResponse.getCookie("token");
        }

        @DisplayName("시간 포맷이 유효하여 성공적으로 요청이 된다.")
        @Test
        void reservationTimeAddTest() {
            Map<String, String> request = Map.of("startAt", "12:00");
            RestAssured.given().log().all()
                    .contentType(ContentType.JSON)
                    .cookie("token", jwtToken)
                    .body(request)
                    .when()
                    .post("/times")
                    .then().log().all()
                    .statusCode(201);
        }
    }
}
