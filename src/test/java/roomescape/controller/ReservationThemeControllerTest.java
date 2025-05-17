package roomescape.controller;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;

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
import org.springframework.test.context.jdbc.Sql;

@Sql(scripts = "/data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
class ReservationThemeControllerTest {

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

        @DisplayName("모든 테마를 성공적으로 가져오므로 200을 던진다.")
        @Test
        void reservationThemeList() {
            // when & then
            RestAssured.given().log().all()
                    .contentType(ContentType.JSON)
                    .cookie("token", jwtToken)
                    .when().get("/themes")
                    .then().log().all()
                    .body("size()", greaterThan(0))
                    .statusCode(200);
        }

        @DisplayName("주간 인기 테마를 성공적으로 가져오므로 200을 던진다.")
        @Test
        void reservationThemeRankingList() {
            // when & then
            RestAssured.given().log().all()
                    .contentType(ContentType.JSON)
                    .cookie("token", jwtToken)
                    .when().get("/themes/ranking")
                    .then().log().all()
                    .body("size()", greaterThan(0))
                    .statusCode(200);
        }

        @DisplayName("테마를 성공적으로 추가하여 201을 던진다.")
        @Test
        void reservationThemeAdd() {
            // given
            Map<String, String> params = Map.of(
                    "name", "테마",
                    "description", "설명",
                    "thumbnail", "썸네일"
            );

            // when & then
            RestAssured.given().log().all()
                    .contentType(ContentType.JSON)
                    .cookie("token", jwtToken)
                    .body(params)
                    .when().post("/themes")
                    .then().log().all()
                    .body("name", is("테마"))
                    .statusCode(201);
        }

        @DisplayName("테마를 성공적으로 삭제하여 204를 던진다.")
        @Test
        void reservationThemeRemove() {
            // when & then
            RestAssured.given().log().all()
                    .contentType(ContentType.JSON)
                    .cookie("token", jwtToken)
                    .when().delete("/themes/{id}", 13L)
                    .then().log().all()
                    .statusCode(204);
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

        @DisplayName("예약과 연관된 테마를 삭제하여 예외가 발생한다")
        @Test
        void reservationThemeRemoveTest() {
            // given
            RestAssured.given().log().all()
                    .contentType(ContentType.JSON)
                    .cookie("token", jwtToken)
                    .when()
                    .delete("/themes/{id}", 1L)
                    .then().log().all()
                    .statusCode(400);
        }
    }
}
