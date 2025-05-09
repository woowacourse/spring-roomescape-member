//package roomescape.theme.ui;
//
//import io.restassured.RestAssured;
//import io.restassured.http.ContentType;
//import java.util.HashMap;
//import java.util.Map;
//import org.assertj.core.api.Assertions;
//import org.junit.jupiter.api.Test;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.HttpStatus;
//import org.springframework.test.annotation.DirtiesContext;
//
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
//@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
//class ThemeRestControllerTest {
//
//    @Test
//    void 테마를_추가한다() {
//        final Map<String, String> params = new HashMap<>();
//        params.put("name", "우가우가");
//        params.put("description", "우가우가 설명");
//        params.put("thumbnail", "따봉우가.jpg");
//
//        RestAssured.given().log().all()
//                .contentType(ContentType.JSON)
//                .body(params)
//                .when().post("/themes")
//                .then().log().all()
//                .statusCode(HttpStatus.CREATED.value());
//    }
//
//    @Test
//    void 테마를_조회한다() {
//        final Map<String, String> params = new HashMap<>();
//        params.put("name", "우가우가");
//        params.put("description", "우가우가 설명");
//        params.put("thumbnail", "따봉우가.jpg");
//
//        final int sizeBeforeCreate = RestAssured.given().log().all()
//                .contentType(ContentType.JSON)
//                .when().get("/themes")
//                .then().log().all()
//                .statusCode(HttpStatus.OK.value())
//                .extract().path("size()");
//
//        RestAssured.given().log().all()
//                .contentType(ContentType.JSON)
//                .body(params)
//                .when().post("/themes")
//                .then().log().all()
//                .statusCode(HttpStatus.CREATED.value());
//
//        final int sizeAfterCreate = RestAssured.given().log().all()
//                .contentType(ContentType.JSON)
//                .when().get("/themes")
//                .then().log().all()
//                .statusCode(HttpStatus.OK.value())
//                .extract()
//                .path("size()");
//
//        Assertions.assertThat(sizeAfterCreate)
//                .isEqualTo(sizeBeforeCreate + 1);
//    }
//
//    @Test
//    void 테마를_삭제한다() {
//        final Map<String, String> params = new HashMap<>();
//        params.put("name", "우가우가");
//        params.put("description", "우가우가 설명");
//        params.put("thumbnail", "따봉우가.jpg");
//
//        final Integer id = RestAssured.given().log().all()
//                .contentType(ContentType.JSON)
//                .body(params)
//                .when().post("/themes")
//                .then().log().all()
//                .statusCode(HttpStatus.CREATED.value())
//                .extract().path("id");
//
//        RestAssured.given().log().all()
//                .contentType(ContentType.JSON)
//                .when().delete("/themes/{id}", id)
//                .then().log().all()
//                .statusCode(HttpStatus.NO_CONTENT.value());
//    }
//
//    @Test
//    void 중복된_이름으로_테마를_생성하면_예외가_발생한다() {
//        final Map<String, String> params = new HashMap<>();
//        params.put("name", "헤일러의 디버깅 교실");
//        params.put("description", "디버깅 재밌어요");
//        params.put("thumbnail", "debug.jpg");
//
//        RestAssured.given().log().all()
//                .contentType(ContentType.JSON)
//                .body(params)
//                .when().post("/themes")
//                .then().log().all()
//                .statusCode(HttpStatus.CREATED.value());
//
//        RestAssured.given().log().all()
//                .contentType(ContentType.JSON)
//                .body(params)
//                .when().post("/themes")
//                .then().log().all()
//                .statusCode(HttpStatus.CONFLICT.value());
//    }
//
//    @Test
//    void 존재하지_않는_테마를_삭제하면_예외가_발생한다() {
//        final Integer id = Integer.MAX_VALUE;
//
//        RestAssured.given().log().all()
//                .contentType(ContentType.JSON)
//                .when().delete("/themes/{id}", id)
//                .then().log().all()
//                .statusCode(HttpStatus.NOT_FOUND.value());
//    }
//}
