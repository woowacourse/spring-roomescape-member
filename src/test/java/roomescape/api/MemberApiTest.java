package roomescape.api;


import static org.hamcrest.Matchers.is;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.auth.Role;
import roomescape.domain.Member;
import roomescape.domain.repository.MemberRepository;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class MemberApiTest {

    @Autowired
    private MemberRepository memberRepository;

    @Test
    void 전체_회원_조회() {
        // given
        Member member = new Member(null, "name", "email1@domain.com", "password1", Role.MEMBER);
        memberRepository.save(member);
        // when & then
        RestAssured.given()
                .contentType(ContentType.JSON)
                .when().get("/members")
                .then()
                .statusCode(200)
                .body("size()", is(1));
    }
}
