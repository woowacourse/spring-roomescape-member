package roomescape.presentation;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.domain.Member;
import roomescape.domain.repository.MemberRepository;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class AuthControllerTest {

    @Autowired
    private MemberRepository memberRepository;

    @Test
    void 로그인_테스트() {
        //given
        memberRepository.save(new Member(null, "name1", "email1@domain.com", "password1"));
        Map<String, Object> body = new HashMap<>();
        body.put("email", "email1@domain.com");
        body.put("password", "password1");

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(body)
                .when().post("/login")
                .then()
                .statusCode(200)
                .cookie("token", notNullValue());
    }

    @Test
    void 비밀번호_불일치_테스트() {
        //given
        memberRepository.save(new Member(null, "name1", "email1@domain.com", "password1"));
        Map<String, Object> body = new HashMap<>();
        body.put("email", "email1@domain.com");
        body.put("password", "password2");

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(body)
                .when().post("/login")
                .then()
                .statusCode(400)
                .body("message", equalTo("이메일 또는 비밀번호를 확인해주세요."));
    }

    @Test
    void 존재하지_않는_이메일_테스트() {
        //given
        Map<String, Object> body = new HashMap<>();
        body.put("email", "email1@domain.com");
        body.put("password", "password2");

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(body)
                .when().post("/login")
                .then()
                .statusCode(400)
                .body("message", equalTo("이메일 또는 비밀번호를 확인해주세요."));
    }

}