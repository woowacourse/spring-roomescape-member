package roomescape.controller;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import roomescape.dto.MemberRequest;

import static org.hamcrest.Matchers.is;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@Sql("/testdata.sql")
class MemberControllerTest {

    @Test
    @DisplayName("전제 회원을 조회할 수 있다.")
    void readMembers() {
        RestAssured.given().log().all()
                .when().get("/members")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(4));
    }

    @Test
    @DisplayName("회원을 추가할 수 있다.")
    void addMember() {
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(new MemberRequest("hello@email.com", "password", "이름"))
                .when().post("/members")
                .then().log().all()
                .statusCode(201);
    }
}
