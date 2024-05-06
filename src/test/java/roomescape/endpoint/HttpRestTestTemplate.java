package roomescape.endpoint;

import static org.hamcrest.Matchers.notNullValue;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.hamcrest.Matcher;
import org.springframework.http.HttpStatus;

class HttpRestTestTemplate {

    private HttpRestTestTemplate() {
    }

    public static Response assertGetOk(String path) {
        return (Response) RestAssured.given().log().all()
                .when().get(path)
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();
    }

    public static Response assertPostCreated(Object params, String path) {
        return (Response) RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post(path)
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .extract();
    }

    public static Response assertPostBadRequest(Object params, String path) {
        return (Response) RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post(path)
                .then().log().all()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .extract();
    }

    public static Response assertDeleteNoContent(String path) {
        return (Response) RestAssured.given().log().all()
                .when().delete(path)
                .then().log().all()
                .statusCode(HttpStatus.NO_CONTENT.value())
                .body(notNullValue())
                .extract();
    }

    public static Response assertDeleteBadRequest(String path) {
        return (Response) RestAssured.given().log().all()
                .when().delete(path)
                .then().log().all()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .extract();
    }
}
