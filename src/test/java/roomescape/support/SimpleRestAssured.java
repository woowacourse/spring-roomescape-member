package roomescape.support;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;

public class SimpleRestAssured {
    public static ValidatableResponse post(String path, Object body) {
        return RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(body)
                .when().post(path)
                .then().log().all();
    }

    public static ValidatableResponse get(String path) {
        return RestAssured.given().log().all()
                .when().get(path)
                .then().log().all();
    }

    public static ValidatableResponse delete(String path) {
        return RestAssured.given().log().all()
                .when().delete(path)
                .then().log().all();
    }
}
