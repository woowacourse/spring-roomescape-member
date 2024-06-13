package roomescape.acceptance;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.domain.member.Member;
import roomescape.domain.member.MemberName;
import roomescape.domain.member.MemberRole;
import roomescape.infrastructure.JwtTokenProvider;
import roomescape.support.AcceptanceTest;

class AdminAcceptanceTest extends AcceptanceTest {
    private static final String PATH = "/admin";
    private static final Member ADMIN = new Member(
            1L,
            new MemberName("어드민"),
            "admin@woo.com",
            "admin",
            MemberRole.ADMIN);

    private JwtTokenProvider jwtTokenProvider = new JwtTokenProvider();
    private String token;

    @BeforeEach
    void setAdmin() {
        token = "token=" + jwtTokenProvider.createToken(ADMIN);
    }

    @DisplayName("[1단계 - 홈 화면] 관리자 메인 페이지를 응답한다")
    @Test
    void step1() {
        RestAssured
                .given()
                .header("cookie", token)
                .when()
                .get(PATH)
                .then()
                .statusCode(200);

    }

    @DisplayName("[2단계 - 예약 조회] 관리자의 예약 관리 페이지를 응답한다")
    @Test
    void step2() {
        RestAssured
                .given()
                .header("cookie", token)
                .when()
                .get(PATH + "/reservation")
                .then()
                .statusCode(200);
    }

    @DisplayName("관리자가 아닌 멤버가 관리자의 예약 관리 페이지에 접근할 수 없다.")
    @Test
    void notAccessMemberToAdminPage() {
        Member user = new Member(
                1L,
                new MemberName("모네"),
                "momo@wooteco.com",
                "mole12",
                MemberRole.USER);
        token = jwtTokenProvider.createToken(user);

        RestAssured
                .given()
                .header("cookie", token)
                .when()
                .get(PATH + "/reservation")
                .then()
                .statusCode(500);
    }
}
