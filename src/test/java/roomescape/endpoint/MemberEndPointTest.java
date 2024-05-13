package roomescape.endpoint;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.notNullValue;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.http.Cookies;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import roomescape.dto.LoginRequest;
import roomescape.dto.MemberRequest;
import roomescape.dto.MemberResponse;
import roomescape.repository.JdbcMemberRepository;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@TestPropertySource(properties = {"spring.config.location = classpath:application-test.yml"})
public class MemberEndPointTest {

    @Autowired
    JdbcMemberRepository memberRepository;
    private Cookies cookies;

    @BeforeEach
    void setAdminToken() {
        cookies = RestAssured.given()
                .contentType(ContentType.JSON)
                .when()
                .body(new LoginRequest("패스워드2", "이메일2"))
                .post("/login")
                .then()
                .statusCode(200)
                .extract()
                .response()
                .getDetailedCookies();
    }

    @DisplayName("관리자가 멤버 목록을 조회하면 상태 코드 200과 테마 목록을 응답으로 반환한다.")
    @Test
    void getMembers() {
        List<MemberResponse> responses = RestAssured.given().log().all()
                .cookies(cookies)
                .when().get("/members")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .jsonPath()
                .getList(".", MemberResponse.class);

        List<MemberResponse> expected = List.of(
                new MemberResponse(1L, "멤버1"),
                new MemberResponse(2L, "멤버2")
        );

        assertThat(responses)
                .isEqualTo(expected);
    }

    @DisplayName("관리자가 멤버를 추가하면 상태 코드 201와 추가된 객체를 반환한다.")
    @Test
    void addMember() {
        MemberRequest request = new MemberRequest("멤버", "이메일", "패스워드");
        MemberResponse expected = new MemberResponse(3L, "멤버");

        MemberResponse response = RestAssured.given().log().all()
                .cookies(cookies)
                .contentType(ContentType.JSON)
                .body(request)
                .when().post("/members")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .extract()
                .as(MemberResponse.class);

        assertThat(response)
                .isEqualTo(expected);
    }

    @DisplayName("관리자가 멤버를 삭제하면 상태 코드 204를 반환한다.")
    @Test
    void deleteMember() {
        RestAssured.given().log().all()
                .cookies(cookies)
                .when().delete("/members/1")
                .then().log().all()
                .statusCode(HttpStatus.NO_CONTENT.value())
                .body(notNullValue());
    }

    @DisplayName("관리자가 예약이 존재하는 멤버를 삭제하면 상태 코드 400을 반환한다.")
    @Test
    void deleteMemberFailed() {
        RestAssured.given().log().all()
                .cookies(cookies)
                .when().delete("/members/2")
                .then().log().all()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }
}
