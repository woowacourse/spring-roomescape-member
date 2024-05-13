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
import static roomescape.service.CookieService.TOKEN;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class AdminReservationPageControllerTest {

    @Autowired
    private MemberRepository memberRepository;

    @Test
    @DisplayName("/admin 페이지를 응답한다.")
    void responseAdminPage() {
        memberRepository.save(ADMIN_1);
        String token = LoginUtils.loginAndGetToken(ADMIN_1);

        RestAssured.given().log().all()
                .cookie(TOKEN, token)
                .when().get("/admin")
                .then().log().all()
                .statusCode(200);

        LoginUtils.logout(ADMIN_1);
    }

    @Test
    @DisplayName("/admin/reservation-new 페이지를 응답한다.")
    void responseAdminReservationPage() {
        memberRepository.save(ADMIN_1);
        String token = LoginUtils.loginAndGetToken(ADMIN_1);

        RestAssured.given().log().all()
                .cookie(TOKEN, token)
                .when().get("/admin/reservation")
                .then().log().all()
                .statusCode(200);

        LoginUtils.logout(ADMIN_1);
    }
}
