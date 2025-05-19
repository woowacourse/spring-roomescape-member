package roomescape.controller;

import static org.hamcrest.Matchers.equalTo;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import java.time.LocalDate;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.jdbc.Sql;

@Sql(scripts = "/data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
class ReservationControllerTest {

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

        @DisplayName("쿼리파라미터가 존재할 때 예약을 필터링해 가져온다.")
        @Test
        void reservationListWhenQueryParamsAreProvided() {
            RestAssured.given()
                    .contentType(ContentType.JSON)
                    .cookie("token", jwtToken)
                    .queryParam("memberId", 1L)
                    .when()
                    .get("/reservations")
                    .then()
                    .statusCode(200)
                    .body("size()", equalTo(3));
        }

        @DisplayName("쿼리파라미터가 존재하지 않을 때 모든 예약을 가져온다.")
        @Test
        void reservationListWhenNoParamsProvided() {
            RestAssured.given()
                    .contentType(ContentType.JSON)
                    .cookie("token", jwtToken)
                    .when()
                    .get("/reservations")
                    .then()
                    .statusCode(200)
                    .body("size()", equalTo(12));
        }
    }

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

        @DisplayName("존재하지 않는 예약을 삭제하려는 경우 404 Not Found를 던진다")
        @Test
        void reservationRemoveTest() {
            //given
            long notExistId = 999;

            //when & then
            RestAssured.given().log().all()
                    .contentType(ContentType.JSON)
                    .cookie("token", jwtToken)
                    .when().delete("/reservations/" + notExistId)
                    .then().log().all()
                    .statusCode(404);
        }

        @DisplayName("이전 시각으로 예약을 요청하는 경우 400 Bad Request를 던진다")
        @Test
        void reservationAddBeforeCurrentDateTime() {
            //given
            Map<String, String> params = Map.of(
                    "date", LocalDate.now().minusDays(1).toString(),
                    "themeId", "1",
                    "timeId", "1"
            );

            //when & then
            RestAssured.given().log().all()
                    .contentType(ContentType.JSON)
                    .cookie("token", jwtToken)
                    .body(params)
                    .when().post("/reservations")
                    .then().log().all()
                    .statusCode(400);
        }

        @DisplayName("같은 날짜 및 시간 예약이 존재하면 400 Bad Request를 던진다")
        @Test
        void reservationAddDuplicatedTest() {
            //given
            Map<String, String> params = Map.of(
                    "date", LocalDate.now().plusDays(1).toString(),
                    "themeId", "1",
                    "timeId", "1"
            );

            RestAssured.given().log().all()
                    .contentType(ContentType.JSON)
                    .cookie("token", jwtToken)
                    .body(params)
                    .when().post("/reservations")
                    .then().log().all()
                    .statusCode(201);

            Map<String, String> duplicated = Map.of(
                    "date", LocalDate.now().plusDays(1).toString(),
                    "themeId", "1",
                    "timeId", "1"
            );

            //when & then
            RestAssured.given().log().all()
                    .contentType(ContentType.JSON)
                    .cookie("token", jwtToken)
                    .body(duplicated)
                    .when().post("/reservations")
                    .then().log().all()
                    .statusCode(400);
        }

        @DisplayName("날짜 포맷이 올바르지 않아 400 Bad request를 던진다")
        @Test
        void reservationAddFormatTest() {
            //given
            Map<String, String> request = Map.of(
                    "date", "2023/08/05",
                    "themeId", "1",
                    "timeId", "1"
            );

            //when & then
            RestAssured.given().log().all()
                    .contentType(ContentType.JSON)
                    .cookie("token", jwtToken)
                    .body(request)
                    .when().post("/reservations")
                    .then().log().all()
                    .statusCode(400);
        }
    }
}
