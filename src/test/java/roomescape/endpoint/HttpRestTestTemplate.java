package roomescape.endpoint;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.hamcrest.Matcher;
import org.springframework.http.HttpStatus;

class HttpRestTestTemplate {

    //TODO: 응답 코드를 하드코딩하지 않기
    private HttpRestTestTemplate() {
    }

    public static void assertGetOk(String path) {
        RestAssured.given().log().all()
                .when().get(path)
                .then().log().all()
                .statusCode(HttpStatus.OK.value());
    }

    public static void assertGetOk(String path, String bodyPath, Matcher<?> matcher) {
        RestAssured.given().log().all()
                .when().get(path)
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .body(bodyPath, matcher);
    }

    public static void assertPostCreated(Object params, String path, String bodyPath, Matcher<?> matcher) {
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post(path)
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .body(bodyPath, matcher);
    }

    public static void assertPostBadRequest(Object params, String path) {
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post(path)
                .then().log().all()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    public static void assertDeleteNoContent(String path) {
        RestAssured.given().log().all()
                .when().delete(path)
                .then().log().all()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }

    public static void assertDeleteBadRequest(String path) {
        RestAssured.given().log().all()
                .when().delete(path)
                .then().log().all()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }
}
