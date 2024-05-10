package roomescape.controller;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.util.Map;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import roomescape.IntegrationTestSupport;
import roomescape.controller.dto.TokenRequest;

class MemberControllerTest extends IntegrationTestSupport {

    public static final String TEST_EMAIL = "test@test.com";
    public static final String TEST_PASSWORD = "1234";
    public static final String TEST_NAME = "테스트";

    String token;
    String createdId;
    int memberSize;

    @LocalServerPort
    int port;

    @DisplayName("회원 CRUD")
    @TestFactory
    Stream<DynamicTest> dynamicUserTestsFromCollection() {
        RestAssured.port = port;

        return Stream.of(
                dynamicTest("어드민으로 토큰 발급", () -> {
                    token = RestAssured
                            .given().log().all()
                            .body(new TokenRequest(ADMIN_EMAIL, ADMIN_PASSWORD))
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .accept(MediaType.APPLICATION_JSON_VALUE)
                            .when().post("/login")
                            .then().log().all().extract().cookie("token");
                }),
                dynamicTest("회원 목록을 조회한다.", () -> {
                    memberSize = RestAssured.given().log().all()
                            .contentType(ContentType.JSON)
                            .cookie("token", token)
                            .when().get("/members")
                            .then().log().all()
                            .statusCode(200).extract()
                            .response().jsonPath().getList("$").size();
                }),
                dynamicTest("이메일 형식이 아니면 회원가입할 수 없다.", () -> {
                    Map<String, String> params = Map.of(
                            "email", "asdfdasf",
                            "password", TEST_PASSWORD,
                            "name", TEST_NAME
                    );

                    RestAssured
                            .given().log().all()
                            .body(params)
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .when().post("/members")
                            .then().log().all()
                            .statusCode(400);
                }),
                dynamicTest("회원가입", () -> {
                    Map<String, String> params = Map.of(
                            "email", TEST_EMAIL,
                            "password", TEST_PASSWORD,
                            "name", TEST_NAME
                    );

                    createdId = RestAssured
                            .given().log().all()
                            .body(params)
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .when().post("/members")
                            .then().log().all()
                            .statusCode(201).extract().header("location").split("/")[2];
                }),
                dynamicTest("회원 목록 개수가 1증가한다.", () -> {
                    RestAssured.given().log().all()
                            .contentType(ContentType.JSON)
                            .cookie("token", token)
                            .when().get("/members")
                            .then().log().all()
                            .statusCode(200).body("size()", is(memberSize + 1));
                }),
                dynamicTest("중복된 이메일로 회원가입할 수 없다.", () -> {
                    Map<String, String> params = Map.of(
                            "email", TEST_EMAIL,
                            "password", TEST_PASSWORD,
                            "name", TEST_NAME
                    );

                    RestAssured
                            .given().log().all()
                            .body(params)
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .when().post("/members")
                            .then().log().all()
                            .statusCode(400);
                }),
                dynamicTest("회원을 삭제한다.", () -> {
                    RestAssured.given().log().all()
                            .contentType(ContentType.JSON)
                            .cookie("token", token)
                            .when().delete("/members/" + createdId)
                            .then().log().all()
                            .statusCode(204);
                }),
                dynamicTest("회원 목록 개수가 1감소한다.", () -> {
                    RestAssured.given().log().all()
                            .contentType(ContentType.JSON)
                            .cookie("token", token)
                            .when().get("/members")
                            .then().log().all()
                            .statusCode(200).body("size()", is(memberSize));
                })
        );
    }
}
