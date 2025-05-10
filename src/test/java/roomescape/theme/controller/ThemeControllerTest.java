package roomescape.theme.controller;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.util.Map;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ThemeControllerTest {

    private static final Map<String, String> RESERVATION_BODY = Map.of(
            "name", "공포 테마",
            "description", "공포 테마 입니다",
            "thumbnail", "url"
    );

    @DisplayName("방 테마 추가 요청시, id를 포함한 방 테마와 CREATED를 응답한다")
    @Test
    void addThemeTest() {
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(RESERVATION_BODY)
                .when().post("/themes")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .body("id", Matchers.equalTo(4))
                .body("name", Matchers.equalTo("공포 테마"));
    }

    @DisplayName("방 테마 조회 요청시, 존재하는 모든 방 테마와 OK를 응답한다")
    @Test
    void findAllThemeTest() {
        RestAssured.given().log().all()
                .when().get("/themes")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .body("size()", Matchers.is(3))
                .body("[0].name", Matchers.equalTo("예시 1"))
                .body("[1].name", Matchers.equalTo("예시 2"))
                .body("[2].name", Matchers.equalTo("예시 3"));
    }


    @DisplayName("방 테마 삭제 요청시, 주어진 아이디에 해당하는 방 테마가 없다면 NOT FOUND를 응답한다.")
    @Test
    void removeThemeTest_WhenThemeDoesNotExisted() {
        RestAssured.given().log().all()
                .when().delete("/themes/1000")
                .then().log().all()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @DisplayName("방 테마 삭제 요청시, 주어진 아이디에 해당하는 방테마가 있다면 삭제하고 NO CONTENT를 응답한다.")
    @Test
    void removeThemeTest() {
        RestAssured.given().log().all()
                .when().delete("/themes/3")
                .then().log().all()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }
}
