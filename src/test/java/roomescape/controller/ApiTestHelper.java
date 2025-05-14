package roomescape.controller;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;

public class ApiTestHelper {

    public static ValidatableResponse post(String path, Object body) {
        return RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(body)
                .when().post(path)
                .then().log().all();
    }

    public static ValidatableResponse post(String path, String token, Object body) {
        return RestAssured.given().log().all()
                .cookie("token", token)
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

    public static ValidatableResponse get(String path, String token) {
        return RestAssured.given().log().all()
                .cookie("token", token)
                .when().get(path)
                .then().log().all();
    }

    public static ValidatableResponse delete(String path) {
        return RestAssured.given().log().all()
                .when().delete(path)
                .then().log().all();
    }

    public static ValidatableResponse delete(String path, String token) {
        return RestAssured.given().log().all()
                .cookie("token", token)
                .when().delete(path)
                .then().log().all();
    }
}
