package roomescape.controller;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.dto.ReservationTimeCreateRequestDto;
import roomescape.dto.ThemeCreateRequestDto;

import static org.hamcrest.Matchers.is;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ThemeControllerTest {

    @Nested
    @DisplayName("테마 조회")
    class ThemeGetTest {

        @DisplayName("테마 목록을 조회한다")
        @Test
        void themesTest() {
            RestAssured.given().log().all()
                    .when().get("/themes")
                    .then().log().all()
                    .statusCode(200)
                    .body("size()", is(0));
        }
    }

    @Nested
    @DisplayName("테마 생성")
    class ThemePostTest {

        @DisplayName("Theme를 생성할 수 있다")
        @Test
        void addThemeTest() {
            ThemeCreateRequestDto requestTheme = new ThemeCreateRequestDto("테마", "설명", "https://");

            RestAssured.given().log().all()
                    .contentType(ContentType.JSON)
                    .body(requestTheme)
                    .when().post("/themes")
                    .then().log().all()
                    .statusCode(201)
                    .body("id", is(1));

            RestAssured.given().log().all()
                    .when().get("/themes")
                    .then().log().all()
                    .statusCode(200)
                    .body("size()", is(1));
        }

        @DisplayName("기존과 동일한 이름을 가진 테마는 생성할 수 없다")
        @Test
        void addThemeExceptionTest() {
            ThemeCreateRequestDto requestTheme = new ThemeCreateRequestDto("테마", "설명", "https://");

            RestAssured.given().log().all()
                    .contentType(ContentType.JSON)
                    .body(requestTheme)
                    .when().post("/themes")
                    .then().log().all()
                    .statusCode(201)
                    .body("id", is(1));

            RestAssured.given().log().all()
                    .contentType(ContentType.JSON)
                    .body(requestTheme)
                    .when().post("/themes")
                    .then().log().all()
                    .statusCode(400);
        }

        @DisplayName("유효하지 않은 name 필드값을 가진 Theme는 생성할 수 없다")
        @ParameterizedTest
        @MethodSource("invalidStrings")
        void invalidRequestThemeTest1(String name) {
            Map<String, String> params = Map.of(
                    "name", name,
                    "description", "hello",
                    "thumbnail", "https://"
            );

            RestAssured.given().log().all()
                    .contentType(ContentType.JSON)
                    .body(params)
                    .when().post("/themes")
                    .then().log().all()
                    .statusCode(400);

        }

        @DisplayName("유효하지 않은 description 필드값을 가진 Theme는 생성할 수 없다")
        @ParameterizedTest
        @MethodSource("invalidStrings")
        void invalidRequestThemeTest2(String description) {
            Map<String, String> params = Map.of(
                    "name", "가이온",
                    "description", description,
                    "thumbnail", "https://"
            );

            RestAssured.given().log().all()
                    .contentType(ContentType.JSON)
                    .body(params)
                    .when().post("/themes")
                    .then().log().all()
                    .statusCode(400);
        }

        static Stream<Arguments> invalidStrings() {
            return Stream.of(
                    Arguments.of(" "),
                    Arguments.of("")
            );
        }

        @DisplayName("유효하지 않은 thumbnail 필드값을 가진 Theme는 생성할 수 없다")
        @ParameterizedTest
        @MethodSource("invalidUrls")
        void invalidRequestThemeTest3(String url) {
            Map<String, String> params = Map.of(
                    "name", "가이온",
                    "description", "hello",
                    "thumbnail", url
            );

            RestAssured.given().log().all()
                    .contentType(ContentType.JSON)
                    .body(params)
                    .when().post("/themes")
                    .then().log().all()
                    .statusCode(400);
        }

        static Stream<Arguments> invalidUrls() {
            return Stream.of(
                    Arguments.of(" "),
                    Arguments.of(""),
                    Arguments.of("ftp://")
            );
        }
    }

    @Nested
    @DisplayName("테마 삭제")
    class DeleteThemeTest {

        @DisplayName("저장된 테마를 삭제할 수 있다")
        @Test
        void deleteThemeTest() {
            ThemeCreateRequestDto requestTheme = new ThemeCreateRequestDto("테마", "설명", "https://");

            RestAssured.given().log().all()
                    .contentType(ContentType.JSON)
                    .body(requestTheme)
                    .when().post("/themes")
                    .then().log().all()
                    .statusCode(201);

            RestAssured.given().log().all()
                    .when().delete("/themes/1")
                    .then().log().all()
                    .statusCode(204);

            RestAssured.given().log().all()
                    .when().get("/themes")
                    .then().log().all()
                    .statusCode(200)
                    .body("size()", is(0));
        }

        @DisplayName("예약된 내역이 존재하는 테마는 삭제할 수 없다")
        @Test
        void deleteThemeExceptionTest() {
            ReservationTimeCreateRequestDto requestTime = new ReservationTimeCreateRequestDto(LocalTime.of(10, 0));
            ThemeCreateRequestDto requestTheme = new ThemeCreateRequestDto("테마", "설명", "https://");
            Map<String, Object> reservationParams = new HashMap<>();
            reservationParams.put("name", "브라운");
            reservationParams.put("date", "2030-08-05");
            reservationParams.put("timeId", 1);
            reservationParams.put("themeId", 1);

            RestAssured.given().log().all()
                    .contentType(ContentType.JSON)
                    .body(requestTime)
                    .when().post("/times")
                    .then().log().all()
                    .statusCode(201);

            RestAssured.given().log().all()
                    .contentType(ContentType.JSON)
                    .body(requestTheme)
                    .when().post("/themes")
                    .then().log().all()
                    .statusCode(201);

            RestAssured.given().log().all()
                    .contentType(ContentType.JSON)
                    .body(reservationParams)
                    .when().post("/reservations")
                    .then().log().all()
                    .statusCode(201);

            RestAssured.given().log().all()
                    .when().delete("/themes/1")
                    .then().log().all()
                    .statusCode(409);
        }

        @DisplayName("존재하지 않는 Id의 Theme 을 삭제할 수 없다")
        @Test
        void invalidThemeIdTest() {
            RestAssured.given().log().all()
                    .when().delete("/themes/5")
                    .then().log().all()
                    .statusCode(404);
        }
    }
}
