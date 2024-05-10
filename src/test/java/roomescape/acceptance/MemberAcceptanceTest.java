package roomescape.acceptance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import roomescape.BasicAcceptanceTest;

class MemberAcceptanceTest extends BasicAcceptanceTest {
    @TestFactory
    @DisplayName("3명의 멤버를 추가한다")
    Stream<DynamicTest> reservationPostTest() {
        return Stream.of(
                dynamicTest("멤버를 추가한다", () -> postMember("name1", "email1", "password1", 201)),
                dynamicTest("멤버를 추가한다", () -> postMember("name2", "email2", "password2", 201)),
                dynamicTest("멤버를 추가한다", () -> postMember("name3", "email3", "password3", 201)),
                dynamicTest("모든 멤버를 조회한다 (총 3명)", () -> getMembers(200, 3))
        );
    }

    private void postMember(String name, String email, String password, int expectedHttpCode) {
        Map<?, ?> requestBody = Map.of("name", name, "email", email, "password", password);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when().post("/members")
                .then().log().all()
                .statusCode(expectedHttpCode);
    }

    private void getMembers(int expectedHttpCode, int expectedReservationTimesSize) {
        Response response = RestAssured.given().log().all()
                .when().get("/members")
                .then().log().all()
                .statusCode(expectedHttpCode)
                .extract().response();

        List<?> reservationTimeResponses = response.as(List.class);

        assertThat(reservationTimeResponses).hasSize(expectedReservationTimesSize);
    }
}
