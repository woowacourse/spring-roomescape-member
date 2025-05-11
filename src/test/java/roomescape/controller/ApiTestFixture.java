package roomescape.controller;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.util.Map;

public class ApiTestFixture {

    public static void createReservationTime() {
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(Map.of("startAt", "10:00"))
                .when().post("/times")
                .then().statusCode(201);
    }

    public static void createTheme() {
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(Map.of(
                        "name", "Ddyong",
                        "description", "살인마가 쫓아오는 느낌",
                        "thumbnail", "https://…"))
                .when().post("/themes")
                .then().statusCode(201);
    }

    public static String loginAndGetToken() {
        return RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(Map.of("email", "abc", "password", "def"))
                .when().post("/auth/login")
                .then().statusCode(200)
                .extract().cookie("token");
    }
}
