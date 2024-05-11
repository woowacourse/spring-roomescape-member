package roomescape.controller.web.admin;

import io.restassured.RestAssured;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.LoginUtils;
import roomescape.repository.member.MemberRepository;

import static roomescape.InitialDataFixture.ADMIN_1;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class AdminThemePageControllerTest {

    @Autowired
    MemberRepository memberRepository;

    @Test
    @DisplayName("/admin/theme 페이지를 응답한다.")
    void responseThemePage() {
        memberRepository.save(ADMIN_1);
        String token = LoginUtils.loginAndGetToken(ADMIN_1);

        RestAssured.given().log().all()
                .cookie("token", token)
                .when().get("/admin/theme")
                .then().log().all()
                .statusCode(200);

        LoginUtils.logout(ADMIN_1);
    }


}
