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
    void 관리자_API로_요청_시_헤더에_관리자_권한_정보가_없으면_예외가_발생한다() {
        RestAssuredMockMvc.given().log().all()
                .contentType(MediaType.APPLICATION_JSON)
                .when().get("/api/admin")
                .then().log().all()
                .status(HttpStatus.FORBIDDEN)
                .body(containsString("관리자만 접근 가능합니다."));
    }

    @Test
    void 관리자_API로_요청_시_헤더에_일반_권한_정보가_있으면_예외가_발생한다() {
        RestAssuredMockMvc.given().log().all()
                .contentType(MediaType.APPLICATION_JSON)
                .header("role", "USER")
                .when().get("/api/admin")
                .then().log().all()
                .status(HttpStatus.FORBIDDEN)
                .body(containsString("관리자만 접근 가능합니다."));
    }

    @Test
    void 관리자_API로_요청_시_헤더에_관리자_권한_정보가_있으면_200_OK를_응답한다() {
        RestAssuredMockMvc.given().log().all()
                .contentType(MediaType.APPLICATION_JSON)
                .header("role", "ADMIN")
                .when().get("/api/admin")
                .then().log().all()
                .status(HttpStatus.OK);
    }
}
