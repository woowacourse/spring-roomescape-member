package roomescape.E2E;

import io.restassured.http.ContentType;
import java.util.Date;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import roomescape.controller.dto.request.MemberLoginRequest;
import roomescape.controller.dto.request.MemberSignUpRequest;
import roomescape.domain.MemberRoleType;
import roomescape.jwt.JwtProvider;
import roomescape.jwt.JwtRequest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles("test")
@Sql(scripts = "/schema.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/reservation-test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class MemberE2ETest {

    @Autowired
    JwtProvider jwtProvider;

    @Test
    @DisplayName("회원 가입을 할 수 있다")
    void signup() {
        //given
        MemberSignUpRequest request = new MemberSignUpRequest("new", "new@email.com", "1234");

        given().log().all()
                .contentType(ContentType.JSON)
                .body(request)
                .when().post("/members")
                .then().log().all()
                .statusCode(200)
                .body("name", is(request.name()));
    }

    @Test
    @DisplayName("이미 사용 중인 이메일은 회원 가입 할 수 없다")
    void cannotRegisterWhenExistedEmail() {
        //given
        MemberSignUpRequest request = new MemberSignUpRequest("member", "member@email.com", "1234");

        //when //then
        given().log().all()
                .contentType(ContentType.JSON)
                .body(request)
                .when().post("/members")
                .then().log().all()
                .statusCode(409);
    }

    @Test
    @DisplayName("사용자의 정보를 기반으로 jwt 토큰을 발행한다")
    void publishAccessToken() {
        //given
        MemberLoginRequest request = new MemberLoginRequest("member@email.com", "1234");

        given().log().all()
                .contentType(ContentType.JSON)
                .body(request)
                .when().post("/login")
                .then().log().all()
                .statusCode(200)
                .cookie("token", notNullValue());
    }

    @Test
    @DisplayName("사용자가 제공한 토큰을 기반으로 검증한다")
    void loginCheck() {
        //given
        MemberLoginRequest request = new MemberLoginRequest("member@email.com", "1234");

        String token = given().log().all()
                .contentType(ContentType.JSON)
                .body(request)
                .when().post("/login")
                .then().log().all()
                .extract().cookie("token");

        given().log().all()
                .cookie("token", token)
                .when().get("/login/check")
                .then().log().all()
                .statusCode(200);
    }

    @Test
    @DisplayName("전체 유저를 조회한다")
    void getAllMember() {

        String token = jwtProvider.generateToken(new JwtRequest(2, "admin", MemberRoleType.ADMIN, new Date()));

        given().log().all()
                .cookie("token", token)
                .when().get("/members")
                .then().log().all()
                .statusCode(200)
                .body("", hasSize(1));
    }
}
