package roomescape.controller.web;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.domain.Email;
import roomescape.domain.Member;
import roomescape.domain.Name;
import roomescape.domain.Role;
import roomescape.repository.member.MemberRepository;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class LoginPageControllerTest {

    @Autowired
    private MemberRepository memberRepository;

    @Test
    @DisplayName("로그인 페이지를 응답한다.")
    void loginPageTest() {
        RestAssured.given().log().all()
                .when().get("/login")
                .then().log().all()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    @DisplayName("로그인 요청을 처리한다.")
    void loginTest() {
        Member member = new Member(new Name("testA"), new Email("email@email.com"), Role.USER, "password");

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

    @Test
    @DisplayName("존재하지 않는 유저의 로그인 요청을 처리한다.")
    void loginFailTest() {
        Member member = new Member(new Name("testA"), new Email("noExist@email.com"), Role.USER, "password");

        Map<String, String> params = new HashMap<>();
        params.put("email", member.getEmail().getEmail());
        params.put("password", member.getPassword());

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/login")
                .then().log().all()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("로그인 조회 요청")
    void loginCheckTest() {
        Member member = new Member(new Name("testA"), new Email("email@email.com"), Role.USER, "password");

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

        String token = response.getCookie("token");
        RestAssured.given().log().all()
                .cookie("token", token)
                .when().get("/login/check")
                .then().log().all()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    @DisplayName("로그아웃을 처리한다.")
    void logoutTest() {
        Member member = new Member(new Name("testA"), new Email("email@email.com"), Role.USER, "password");

        memberRepository.save(member);
        Map<String, String> params = new HashMap<>();
        params.put("email", member.getEmail().getEmail());
        params.put("password", member.getPassword());

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/login")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract().response();

        Response response = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/logout")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract().response();

        assertThat(response.cookie("token")).isBlank();
    }

}