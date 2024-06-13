package roomescape.admin;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;

import io.restassured.RestAssured;
import roomescape.member.JwtTokenProvider;
import roomescape.member.domain.Member;
import roomescape.member.repository.MemberDao;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class AdminPageControllerTest {

    @Autowired
    private MemberDao memberDao;
    @Autowired
    private JwtTokenProvider tokenProvider;
    private Member admin;

    @BeforeEach
    void setUpAdminMember() {
        admin = memberDao.findMemberById(2L).get();
    }

    @Test
    @DisplayName("방탈출 관리 홈페이지를 매핑한다.")
    void index() {
        String token = tokenProvider.createToken(admin);

        RestAssured.given().log().all()
                .cookie("token", token)
                .when().get("/admin")
                .then().log().all()
                .statusCode(200);
    }

    @Test
    @DisplayName("방탈출 예약 관리 페이지를 매핑한다.")
    void reservation() {
        String token = tokenProvider.createToken(admin);

        RestAssured.given().log().all()
                .cookie("token", token)
                .when().get("/admin/reservation")
                .then().log().all()
                .statusCode(200);
    }

    @Test
    @DisplayName("방탈출 테마 관리 페이지를 매핑한다.")
    void theme() {
        String token = tokenProvider.createToken(admin);

        RestAssured.given().log().all()
                .cookie("token", token)
                .when().get("/admin/theme")
                .then().log().all()
                .statusCode(200);
    }
}
