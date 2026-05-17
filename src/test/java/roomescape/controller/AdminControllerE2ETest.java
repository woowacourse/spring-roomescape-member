package roomescape.controller;

import static org.hamcrest.Matchers.is;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.util.Map;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class AdminControllerE2ETest {

    @Nested
    class 예약_조회_케이스 {

        @DisplayName("모든 예약을 조회한다")
        @Sql("/data.sql")
        @Test
        void 모든_예약을_조회한다() {
            RestAssured.given().log().all()
                    .when().get("/admin/reservations")
                    .then().log().all()
                    .statusCode(200)
                    .body("size()", is(24));
        }

        @DisplayName("예약 ID로 상세 정보를 조회한다")
        @Sql("/data.sql")
        @Test
        void 예약_ID로_상세_정보를_조회한다() {
            RestAssured.given().log().all()
                    .when().get("/admin/reservations/1")
                    .then().log().all()
                    .statusCode(200)
                    .body("id", is(1))
                    .body("name", is("루드비코"));
        }

        @DisplayName("존재하지 않는 예약 ID 조회 시 422 Unprocessable Entity를 응답한다")
        @Test
        void 존재하지_않는_예약_ID_조회_시_422를_응답한다() {
            RestAssured.given().log().all()
                    .when().get("/admin/reservations/" + Long.MAX_VALUE)
                    .then().log().all()
                    .statusCode(422);
        }

        @DisplayName("잘못된 형식의 예약 ID 조회 시 400 Bad Request를 응답한다")
        @Test
        void 잘못된_형식의_예약_ID_조회_시_400을_응답한다() {
            RestAssured.given().log().all()
                    .when().get("/admin/reservations/invalid-id")
                    .then().log().all()
                    .statusCode(400)
                    .body("title", is("타입 불일치"));
        }
    }

    @Nested
    class 예약_시간_생성_삭제_케이스 {

        @DisplayName("예약 시간을 생성한다")
        @Test
        void 예약_시간_생성에_성공하면_201_Created를_응답한다() {
            Map<String, String> requestBody = Map.of(
                    "startAt", "10:00"
            );

            RestAssured.given().log().all()
                    .contentType(ContentType.JSON)
                    .body(requestBody)
                    .when().post("/admin/times")
                    .then().log().all()
                    .statusCode(201)
                    .header("Location", "/times/1");
        }

        @DisplayName("예약 시간 삭제에 성공하면 204 No Content를 응답한다")
        @Sql("/initialize_theme_and_time.sql")
        @Test
        void 예약_시간_삭제에_성공하면_204를_응답한다() {
            RestAssured.given().log().all()
                    .contentType(ContentType.JSON)
                    .when().delete("/admin/times/1")
                    .then().log().all()
                    .statusCode(204);
        }

        @DisplayName("잘못된 시간 형식으로 시간 생성을 요청하면 422 Unprocessable Entity를 응답한다")
        @Test
        void 잘못된_시간_형식으로_시간_생성을_요청하면_422를_응답한다() {
            Map<String, String> requestBodyWithIllegalTimeFormat = Map.of(
                    "startAt", "10-00"
            );

            RestAssured.given().log().all()
                    .contentType(ContentType.JSON)
                    .body(requestBodyWithIllegalTimeFormat)
                    .when().post("/admin/times")
                    .then().log().all()
                    .statusCode(422);
        }

        @DisplayName("예약이 존재하는 시간 삭제 요청 시 409 Conflict를 응답한다")
        @Sql("/data.sql")
        @Test
        void 삭제하려는_예약_시간에_대한_예약이_존재한다면_409를_응답한다() {
            RestAssured.given().log().all()
                    .contentType(ContentType.JSON)
                    .when().delete("/admin/times/1")
                    .then().log().all()
                    .statusCode(409);
        }

        @DisplayName("존재하지 않는 예약 시간 삭제 요청 시 422 Unprocessable Entity를 응답한다")
        @Test
        void 삭제하려는_예약_시간이_존재하지_않는다면_422를_응답한다() {
            RestAssured.given().log().all()
                    .contentType(ContentType.JSON)
                    .when().delete("/admin/times/1")
                    .then().log().all()
                    .statusCode(422);
        }

        @DisplayName("예약 시간 생성 시 필수 파라미터가 누락되면 400 Bad Request를 응답한다")
        @Test
        void 예약_시간_생성_시_시간이_누락되면_400을_응답한다() {
            Map<String, String> requestBody = Map.of(); // empty body

            RestAssured.given().log().all()
                    .contentType(ContentType.JSON)
                    .body(requestBody)
                    .when().post("/admin/times")
                    .then().log().all()
                    .statusCode(400);
        }
    }

    @Nested
    class 테마_생성_삭제_케이스 {

        @DisplayName("테마를 생성한다")
        @Test
        void 테마_생성에_성공하면_201_Created를_응답한다() {
            Map<String, String> requestBody = Map.of(
                    "name", "귀신찾기",
                    "description", "귀신을 찾는 테마입니다.",
                    "imageUrl", "https://image.png"
            );

            RestAssured.given().log().all()
                    .contentType(ContentType.JSON)
                    .body(requestBody)
                    .when().post("/admin/themes")
                    .then().log().all()
                    .statusCode(201)
                    .header(HttpHeaders.LOCATION, "/themes/1");
        }

        @DisplayName("테마 생성 시 필수 데이터가 누락되면 400 Bad Request를 응답한다")
        @ParameterizedTest(name = "{0}")
        @MethodSource("provideInvalidThemeRequests")
        void 테마_생성_시_필수_데이터가_누락되면_400을_응답한다(String description, Map<String, String> invalidRequest) {
            RestAssured.given().log().all()
                    .contentType(ContentType.JSON)
                    .body(invalidRequest)
                    .when().post("/admin/themes")
                    .then().log().all()
                    .statusCode(400);
        }

        private static Stream<Arguments> provideInvalidThemeRequests() {
            return Stream.of(
                    Arguments.of("이름 누락", Map.of("description", "설명", "imageUrl", "https://image.png")),
                    Arguments.of("설명 누락", Map.of("name", "이름", "imageUrl", "https://image.png")),
                    Arguments.of("이미지 누락", Map.of("name", "이름", "description", "설명"))
            );
        }

        @DisplayName("테마 삭제에 성공하면 204 No Content를 응답한다")
        @Sql("/initialize_theme_and_time.sql")
        @Test
        void 테마_삭제에_성공하면_204를_응답한다() {
            RestAssured.given().log().all()
                    .contentType(ContentType.JSON)
                    .when().delete("/admin/themes/1")
                    .then().log().all()
                    .statusCode(204);
        }

        @DisplayName("존재하지 않는 테마 삭제 요청 시 422 Unprocessable Entity를 응답한다")
        @Test
        void 삭제하려는_테마가_존재하지_않는다면_422를_응답한다() {
            RestAssured.given().log().all()
                    .contentType(ContentType.JSON)
                    .when().delete("/admin/themes/1")
                    .then().log().all()
                    .statusCode(422);
        }

        @DisplayName("예약이 존재하는 테마 삭제 요청 시 409 Conflict를 응답한다")
        @Sql("/data.sql")
        @Test
        void 삭제하려는_테마에_대한_예약이_존재한다면_409를_응답한다() {
            RestAssured.given().log().all()
                    .contentType(ContentType.JSON)
                    .when().delete("/admin/themes/1")
                    .then().log().all()
                    .statusCode(409);
        }
    }
}
