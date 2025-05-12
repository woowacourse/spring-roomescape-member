package roomescape.controller;

import static org.hamcrest.Matchers.is;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class MemberControllerTest {

    @Test
    @DisplayName("모든 멤버 요청하면 멤버들과 200을 반환한다")
    void getMembers() {
        ApiTestFixture.signUpAdmin("admin@naver.com", "password", "vector");
        ApiTestHelper.get("/members")
                .statusCode(200)
                .body("size()", is(1));
    }

}
