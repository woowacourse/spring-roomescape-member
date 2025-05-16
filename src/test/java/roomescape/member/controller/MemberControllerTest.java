package roomescape.member.controller;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.util.Map;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class MemberControllerTest {

    private static final Map<String, String> MEMBER_BODY = Map.of(
            "email", "razel@email.com",
            "password", "razelpassword",
            "name", "razel"
    );

    @DisplayName("멤버 추가 요청시, id를 포함한 멤버와 CREATED를 응답한다")
    @Test
    void addMemberTest() {
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(MEMBER_BODY)
                .when().post("/members")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .body("name", Matchers.equalTo("razel"));
    }

    @DisplayName("예약 조회 요청시, 존재하는 모든 예약과 OK를 응답한다")
    @Test
    void findAllMembersTest() {
        RestAssured.given().log().all()
                .when().get("/members")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .body("size()", Matchers.is(2))
                .body("[0].name", Matchers.equalTo("어드민"))
                .body("[1].name", Matchers.equalTo("사용자"));
    }
}
