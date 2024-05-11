package roomescape.controller.render;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import roomescape.auth.token.TokenProvider;
import roomescape.model.Member;
import roomescape.model.Role;
import roomescape.repository.MemberRepository;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AdminControllerTest {

    @LocalServerPort
    int port;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private TokenProvider tokenProvider;

    @BeforeEach
    void initPort() {
        RestAssured.port = port;
    }

    @DisplayName("홈 화면 조회")
    @Test
    void home() {
        final Member member = memberRepository.save(new Member("감자", Role.ADMIN, "111@aaa.com", "abc1234"));
        final String token = tokenProvider.createToken(member);
        RestAssured.given().log().all()
                .cookie("token", token)
                .when().get("/admin")
                .then().log().all()
                .statusCode(200);
    }

    @DisplayName("예약 화면 조회")
    @Test
    void reservation() {
        final Member member = memberRepository.save(new Member("감자", Role.ADMIN, "111@aaa.com", "abc1234"));
        final String token = tokenProvider.createToken(member);
        RestAssured.given().log().all()
                .cookie("token", token)
                .when().get("/admin/reservation")
                .then().log().all()
                .statusCode(200);
    }

    @DisplayName("시간 화면 조회")
    @Test
    void time() {
        final Member member = memberRepository.save(new Member("감자", Role.ADMIN, "111@aaa.com", "abc1234"));
        final String token = tokenProvider.createToken(member);
        RestAssured.given().log().all()
                .cookie("token", token)
                .when().get("/admin/time")
                .then().log().all()
                .statusCode(200);
    }

    @DisplayName("테마 화면 조회")
    @Test
    void theme() {
        final Member member = memberRepository.save(new Member("감자", Role.ADMIN, "111@aaa.com", "abc1234"));
        final String token = tokenProvider.createToken(member);
        RestAssured.given().log().all()
                .cookie("token", token)
                .when().get("/admin/theme")
                .then().log().all()
                .statusCode(200);
    }
}
