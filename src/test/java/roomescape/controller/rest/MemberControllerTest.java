package roomescape.controller.rest;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;

import io.restassured.RestAssured;
import roomescape.BaseTest;
import roomescape.domain.Member;

class MemberControllerTest extends BaseTest {

    @Test
    void findAll() {
        List<Member> members = RestAssured.given().log().all()
                .when().get("/members")
                .then().log().all()
                .statusCode(200).extract()
                .jsonPath().getList(".", Member.class);
        assertEquals(3, members.size());
    }
}
