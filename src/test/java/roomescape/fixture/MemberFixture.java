package roomescape.fixture;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.Map;
import roomescape.domain.Member;

public class MemberFixture {

    public static Member getDomain() {
        return Member.of(null, "제리", "jerry@email.com", "1234", "ADMIN");
    }

    public static Long createAndReturnId() {
        final Map<String, Object> member = new HashMap<>();
        member.put("name", "제리");
        member.put("email", "jerry@email.com");
        member.put("password", "1234");
        member.put("role", "ADMIN");

        final Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(member)
                .when().post("/members");

        return Long.parseLong(response.then().extract().jsonPath().getString("id"));
    }
}
