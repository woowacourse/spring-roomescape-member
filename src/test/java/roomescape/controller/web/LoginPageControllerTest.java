package roomescape.controller.web;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import roomescape.domain.Email;
import roomescape.domain.Member;
import roomescape.domain.Name;
import roomescape.repository.member.MemberRepository;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class LoginPageControllerTest {

    @Autowired
    private MemberRepository memberRepository;

    @DisplayName("로그인 페이지를 응답한다.")
    @Test
    void loginPageTest() {
        RestAssured.given().log().all()
                .when().get("/login")
                .then().log().all()
                .statusCode(HttpStatus.OK.value());
    }

    @DisplayName("로그인 요청을 처리한다.")
    @Test
    void loginTest() {
        Member member = new Member(new Name("testA"), new Email("email@email.com"), "password");

        memberRepository.save(member);

        Map<String, String> params = new HashMap<>();
        params.put("email", member.getEmail().getEmail());
        params.put("password", member.getPassword());

        Response response = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/login")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract().response();

        assertThat(response.getCookie("token")).isNotBlank();
    }
}