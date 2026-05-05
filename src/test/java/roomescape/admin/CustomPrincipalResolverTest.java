package roomescape.admin;

import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.context.WebApplicationContext;
import roomescape.admin.dummy.DummyController;

@WebMvcTest(DummyController.class)
class CustomPrincipalResolverTest {

    @BeforeEach
    void setUp(WebApplicationContext webApplicationContext) {
        RestAssuredMockMvc.webAppContextSetup(webApplicationContext);
    }

    @Test
    void 헤더에_사용자_권한_정보가_없는_경우_예외가_발생한다() {
        RestAssuredMockMvc.given().log().all()
                .contentType(MediaType.APPLICATION_JSON)
                .when().get("/dummy/access")
                .then().log().all()
                .status(HttpStatus.FORBIDDEN);
    }

    @Test
    void 헤더에_사용자_권한_정보가_있다면_예외가_정상_응답을_한다() {
        RestAssuredMockMvc.given().log().all()
                .contentType(MediaType.APPLICATION_JSON)
                .header("role", "any")
                .when().get("/dummy/access")
                .then().log().all()
                .status(HttpStatus.OK);
    }
}
