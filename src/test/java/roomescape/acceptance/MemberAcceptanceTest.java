package roomescape.acceptance;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class MemberAcceptanceTest extends BasicAcceptanceTest {
    @DisplayName("모든 사용자를 조회한다")
    @Sql("/test-data/members.sql")
    @Test
    void when_findAllMembers_then_returnMembers() {
        // when
        Response response = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .when().get("/members")
                .then().log().all()
                .statusCode(200)
                .extract().response();

        List<Object> members = response.jsonPath().getList("members");

        // then
        assertThat(members.size()).isEqualTo(2);
    }
}
