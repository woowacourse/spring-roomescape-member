package roomescape.controller;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.time.LocalTime;
import java.util.Map;
import roomescape.dto.request.LoginMemberRequest;
import roomescape.dto.request.ReservationTimeRequest;
import roomescape.dto.request.SignUpRequest;

public class ApiTestFixture {


    public static void createReservationTime(LocalTime startAt) {
        new ReservationTimeRequest(startAt);
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(new ReservationTimeRequest(startAt))
                .when().post("/times")
                .then().statusCode(201);
    }

    public static void createTheme(String name, String description, String thumbnail) {
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(Map.of(
                        "name", name,
                        "description", description,
                        "thumbnail", thumbnail))
                .when().post("/themes")
                .then().statusCode(201);
    }

    public static void signUpUser(String email, String password, String name) {
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(new SignUpRequest(email, password, name))
                .when().post("/signup")
                .then().statusCode(201);
    }

    public static void signUpAdmin(String email, String password, String name) {
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(new SignUpRequest(email, password, name))
                .when().post("/adminSignup")
                .then().statusCode(201);
    }

    public static String loginAndGetToken(String email, String password) {
        return RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(new LoginMemberRequest(email, password))
                .when().post("/auth/login")
                .then().statusCode(200)
                .extract().cookie("token");
    }
}
