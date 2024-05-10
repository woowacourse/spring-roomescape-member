package roomescape.fixture;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.Map;
import roomescape.domain.Theme;
import roomescape.service.dto.input.ThemeInput;

public class ThemeFixture {

    public static Long createAndReturnId(final String name) {
        final Map<String, String> params = new HashMap<>();
        params.put("name", name);
        params.put("description", "테마 설명");
        params.put("thumbnail", "image.jpg");

        final Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/themes");

        return Long.parseLong(response.then().extract().jsonPath().getString("id"));
    }

    public static ThemeInput getInput(final String name) {
        return new ThemeInput(
                name,
                "우테코 레벨2를 탈출하는 내용입니다.",
                "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg"
        );
    }

    public static Theme getDomain(final String name) {
        return Theme.of(
                null,
                name,
                "우테코 레벨2를 탈출하는 내용입니다.",
                "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg"
        );
    }
}
