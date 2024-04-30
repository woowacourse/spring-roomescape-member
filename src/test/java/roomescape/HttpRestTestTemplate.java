package roomescape;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;

import static org.hamcrest.Matchers.is;

class HttpRestTestTemplate {

    private HttpRestTestTemplate() {
    }

    public static void assertGetOk(String path) {
        RestAssured.given().log().all()
                .when().get(path)
                .then().log().all()
                .statusCode(200);
    }

    public static void assertGetOk(String path, String bodyPath, Integer expectedValue) {
        RestAssured.given().log().all()
                .when().get(path)
                .then().log().all()
                .statusCode(200)
                .body(bodyPath, is(expectedValue));
    }

    public static void assertPostCreated(Object params, String path, String bodyPath, Integer expectedValue) {
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post(path)
                .then().log().all()
                .statusCode(201)
                .body(bodyPath, is(expectedValue));
    }

    public static void assertDeleteNoContent(String path) {
        RestAssured.given().log().all()
                .when().delete(path)
                .then().log().all()
                .statusCode(204);
    }

    public static void assertDeleteBadRequest(String path) {
        RestAssured.given().log().all()
                .when().delete(path)
                .then().log().all()
                .statusCode(400);
    }

    public static void assertPostBadRequest(Object params, String path) {
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post(path)
                .then().log().all()
                .statusCode(400);
    }
}
