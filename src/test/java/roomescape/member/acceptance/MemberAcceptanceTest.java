package roomescape.member.acceptance;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.member.dto.request.MemberRequest.MemberCreateRequest;
import roomescape.member.entity.Member;
import roomescape.member.entity.RoleType;
import roomescape.member.unit.repository.MemberRepository;

// @formatter:off
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class MemberAcceptanceTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MemberRepository memberRepository;

    @Test
    @DisplayName("회원을 생성한다.")
    void createMember() throws Exception {
        // given
        var request = new MemberCreateRequest("미소", "miso@email.com", "password");

        // when & then
        given()
                .contentType(ContentType.JSON)
                .body(objectMapper.writeValueAsString(request))
        .when()
                .post("/members")
        .then()
                .statusCode(200)
                .body("name", equalTo("미소"))
                .body("email", equalTo("miso@email.com"))
                .body("role", equalTo("USER"));
    }

    @Test
    @DisplayName("모든 회원을 조회한다.")
    void getAllMembers() throws Exception {
        // given
        var member1 = new Member(0L, "미소", "miso@email.com", "password", RoleType.USER);
        var member2 = new Member(0L, "브라운", "brown@email.com", "password", RoleType.USER);
        memberRepository.save(member1);
        memberRepository.save(member2);

        // when & then
        given()
        .when()
                .get("/members")
        .then()
                .statusCode(200)
                .body("$", hasSize(2))
                .body("[0].name", equalTo("미소"))
                .body("[1].name", equalTo("브라운"));
    }

    @Test
    @DisplayName("회원을 삭제한다.")
    void deleteMember() throws Exception {
        // given
        var member = new Member(0L, "미소", "miso@email.com", "password", RoleType.USER);
        var savedMember = memberRepository.save(member);

        // when & then
        given()
        .when()
                .delete("/members/" + savedMember.getId())
        .then()
                .statusCode(200);

        given()
        .when()
                .get("/members")
        .then()
                .statusCode(200)
                .body("$", hasSize(0));
    }

    @Test
    @DisplayName("존재하지 않는 회원을 삭제하면 예외가 발생한다.")
    void deleteNonExistentMember() {
        // when & then
        given()
        .when()
                .delete("/members/1")
        .then()
                .statusCode(404)
                .body("message", equalTo("존재하지 않는 id 입니다."));
    }
}
