package roomescape.controller;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.lessThanOrEqualTo;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.dto.ThemeRequest;
import roomescape.exception.ErrorCode;

@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ThemeControllerTest {

    @Test
    public void 테마_전체_조회_API() {
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .when().get("/themes")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(17));
    }

    @Test
    public void 테마_추가_API() {
        ThemeRequest themeRequest = new ThemeRequest("공포 영화", "테스트입니다", "url");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(themeRequest)
                .when().post("admin/themes")
                .then().log().all()
                .statusCode(201)
                .body("size()", is(4));
    }

    @Test
    public void 특정_테마_삭제_API() {
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .when().delete("admin/themes/14")
                .then().log().all()
                .statusCode(204);
    }

    @Test
    public void 인기_테마_조회_API() {
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .when().get("/themes/ranks")
                .then().log().all()
                .statusCode(200)
                .body("size()", lessThanOrEqualTo(10));
    }

    @ParameterizedTest
    @MethodSource("emptyThemeRequest")
    public void 테마_등록_시_필드가_빈값이면_400을_반환한다(ThemeRequest themeRequest) {
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(themeRequest)
                .when().post("admin/themes")
                .then().log().all()
                .statusCode(400);
    }


    @Test
    public void 테마_등록_시_이름이_중복이면_409를_반환한다() {
        ThemeRequest themeRequest = new ThemeRequest("붉은 요람", "테스트입니다", "url");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(themeRequest)
                .when().post("admin/themes")
                .then().log().all()
                .statusCode(409)
                .body("message", is(ErrorCode.THEME_DUPLICATED.getMessage()));
    }

    @Test
    public void 테마_삭제_시_존재하지_않는_테마를_삭제하는_경우_404를_반환한다() {
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .when().delete("admin/themes/-1")
                .then().log().all()
                .statusCode(404)
                .body("message", is(ErrorCode.THEME_NOT_FOUND.getMessage()));
    }

    @Test
    public void 테마_삭제_시_예약이_존재하는_테마를_삭제하는_경우_409를_반환한다() {
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .when().delete("admin/themes/1")
                .then().log().all()
                .statusCode(409)
                .body("message", is(ErrorCode.THEME_HAS_RESERVATIONS.getMessage()));
    }

    private static Stream<ThemeRequest> emptyThemeRequest() {
        return Stream.of(
                new ThemeRequest("", "테스트입니다", "url"),
                new ThemeRequest("붉은 악마", "", "url"),
                new ThemeRequest("붉은 악마", "테스트입니다", "")
        );
    }

}
