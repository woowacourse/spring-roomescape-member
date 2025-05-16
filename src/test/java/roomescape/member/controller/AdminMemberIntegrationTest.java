package roomescape.member.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertAll;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.auth.controller.LoginController;
import roomescape.auth.service.AuthService;
import roomescape.member.domain.Member;
import roomescape.member.dto.MemberResponse;
import roomescape.member.repository.MemberRepository;
import roomescape.util.fixture.AuthFixture;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class AdminMemberIntegrationTest {

    @Autowired
    private AuthService authService;

    @Autowired
    private MemberRepository memberRepository;

    private String token;

    @BeforeEach
    void setup() {
        token = AuthFixture.createAdminToken(authService);
    }

    @DisplayName("전체 사용자 목록을 조회한다")
    @Test
    void get_members_test() {
        // when
        List<MemberResponse> findMembers = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .cookie(LoginController.TOKEN_COOKIE_NAME, token)
                .when().get("/admin/members")
                .then().log().all()
                .statusCode(200)
                .extract()
                .jsonPath()
                .getList(".", MemberResponse.class);

        // then
        List<Member> members = memberRepository.findAll();
        assertAll(
                () -> assertThat(findMembers).hasSize(members.size()),
                () -> assertThat(findMembers).extracting(MemberResponse::email)
                        .containsExactlyInAnyOrderElementsOf(
                                members.stream()
                                        .map(Member::getEmail)
                                        .toList()
                        ),
                () -> assertThat(findMembers).extracting(MemberResponse::name)
                        .containsExactlyInAnyOrderElementsOf(
                                members.stream()
                                        .map(Member::getName)
                                        .toList()
                        )
        );
    }

    @DisplayName("권한이 없는 사용자가 접근을 시도하면 오류가 발생한다")
    @Test
    void access_denied_for_non_admin_test() {
        // given
        String userToken = AuthFixture.createUserToken(authService);

        // when & then
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .cookie(LoginController.TOKEN_COOKIE_NAME, userToken)
                .when().get("/admin/members")
                .then().log().all()
                .statusCode(403)
                .body(equalTo("접근 권한이 없습니다."));
    }

}
