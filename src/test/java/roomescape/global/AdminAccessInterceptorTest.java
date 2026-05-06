package roomescape.global;

import static org.hamcrest.Matchers.containsString;

import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.context.WebApplicationContext;

@WebMvcTest(DummyController.class)
class AdminAccessInterceptorTest {

    @BeforeEach
    void setUp(WebApplicationContext webApplicationContext) {
        RestAssuredMockMvc.webAppContextSetup(webApplicationContext);
    }

    @Test
    void 관리자_Prefix_Api로_요청시_헤더에_관리자_권한_정보가_없는_경우_예외가_발생한다() {
        RestAssuredMockMvc.given().log().all()
                .contentType(MediaType.APPLICATION_JSON)
                .when().get("/api/admin")
                .then().log().all()
                .status(HttpStatus.FORBIDDEN)
                .body(containsString("관리자만 접근 가능합니다."));
    }

    @Test
    void 관리자_Prefix_Api로_요청시_헤더에_관리자가_아닌_일반_권한_정보가_있는_경우_예외가_발생한다() {
        RestAssuredMockMvc.given().log().all()
                .contentType(MediaType.APPLICATION_JSON)
                .header("role", "USER")
                .when().get("/api/admin")
                .then().log().all()
                .status(HttpStatus.FORBIDDEN)
                .body(containsString("관리자만 접근 가능합니다."));
    }

    @Test
    void 관리자_Prefix_Api로_요청시_헤더에_관리자_권한_정보가_있다면_200OK_정상_응답을_한다() {
        RestAssuredMockMvc.given().log().all()
                .contentType(MediaType.APPLICATION_JSON)
                .header("role", "ADMIN")
                .when().get("/api/admin")
                .then().log().all()
                .status(HttpStatus.OK);
    }
}
