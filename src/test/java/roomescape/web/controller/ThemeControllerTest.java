package roomescape.web.controller;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.startsWith;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.jdbc.Sql;
import roomescape.service.request.ThemeRequest;
import roomescape.support.IntegrationTestSupport;

class ThemeControllerTest extends IntegrationTestSupport {

    @Test
    @DisplayName("전체 테마 목록을 조회한다.")
    void getAll() {
        RestAssured.given().log().all()
                .when().get("/themes")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(2));
    }

    // 테스트 데이터 - 테마아이디(해당테마 예약 갯수): 6(6), 5(5), 7(5), 4(4), 3(3), 1(1), 8(1), 2(0), 9(0), 10(0)
    @Test
    @Sql(scripts = {"/data.sql"})
    @DisplayName("인기 테마 목록을 조회한다.")
    void popularThemes() {
        RestAssured.given().log().all()
                .when().get("/themes?showRanking=true")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(10))
                .body("id", contains(6, 5, 7, 4, 3, 1, 8, 2, 9, 10));
    }

    @Test
    @DisplayName("테마를 생성한다.")
    void create() {
        ThemeRequest request = new ThemeRequest("name", "description", "thumbnail");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(request)
                .when().post("/themes")
                .then().log().all()
                .statusCode(201)
                .header("Location", startsWith("/themes/"))
                .body("name", is("name"))
                .body("description", is("description"))
                .body("thumbnail", is("thumbnail"));
    }

    @Test
    @DisplayName("생성할 테마 이름은 필수이다.")
    void validateName() {
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body("{}")
                .when().post("/themes")
                .then().log().all()
                .statusCode(400)
                .body("details.message", hasItem("테마 이름은 필수입니다."));
    }

    @Test
    @DisplayName("생성할 테마 설명은 필수이다.")
    void validateDescription() {
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body("{}")
                .when().post("/themes")
                .then().log().all()
                .statusCode(400)
                .body("details.message", hasItem("테마 설명은 필수입니다."));
    }

    @Test
    @DisplayName("생성할 테마 썸네일은 필수이다.")
    void validateThumbnail() {
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body("{}")
                .when().post("/themes")
                .then().log().all()
                .statusCode(400)
                .body("details.message", hasItem("테마 썸네일은 필수입니다."));
    }

    @Test
    @DisplayName("테마를 삭제한다.")
    void delete() {
        RestAssured.given().log().all()
                .when().delete("/themes/" + 테마_2번_ID)
                .then().log().all()
                .statusCode(204);

        RestAssured.given().log().all()
                .when().get("/themes")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(1));
    }

    @Test
    @DisplayName("사용되고 있는 테마는 삭제할 수 없다.")
    void usedDelete() {
        RestAssured.given().log().all()
                .when().delete("/themes/" + 테마_1번_ID)
                .then().log().all()
                .statusCode(400)
                .body("message", is("해당 테마를 사용하는 예약이 존재합니다."));
    }
}
