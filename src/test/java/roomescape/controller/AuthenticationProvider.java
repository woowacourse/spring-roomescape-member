package roomescape.controller;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;

import java.util.Map;

public class AuthenticationProvider {

    public static String login(String email, String password) {
        Map<String, String> params = Map.of(
                "email", email,
                "password", password
        );

        String header = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/login")
                .then().log().all()
                .statusCode(200)
                .extract()
                .header("Set-Cookie");

        String[] parts = header.split(";");
        return parts[0].split("=")[1];
    }

    public static String loginAdmin() {
        return login("admin@example.com", "woowa123!");
    }

    public static String loginMember() {
        return login("naknak@example.com", "nak123");
    }
}
