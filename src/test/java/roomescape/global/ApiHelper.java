package roomescape.global;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import roomescape.auth.presentation.dto.LoginRequest;
import roomescape.member.domain.Role;
import roomescape.member.presentation.dto.RegisterRequest;
import roomescape.member.presentation.fixture.MemberFixture;

public final class ApiHelper {

    public static final String THEME_ENDPOINT = "/themes";
    public static final String TIME_ENDPOINT = "/times";
    public static final String RESERVATION_ENDPOINT = "/reservations";
    public static final String MEMBER_ENDPOINT = "/members";

    public static Response post(final String endpoint, final Object body) {
        return setRequest(body)
                .when().post(endpoint);
    }

    public static Response postWithToken(final String endpoint, final Object body, final String token) {
        return setRequest(body)
                .cookie("token", token)
                .when().post(endpoint);
    }

    private static RequestSpecification setRequest(Object body) {
        return RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(body);
    }

    public static String getUserToken() {
        return getToken(Role.USER);
    }

    public static String getAdminToken() {
        return getToken(Role.ADMIN);
    }

    private static String getToken(final Role role) {
        MemberFixture memberFixture = new MemberFixture();

        RegisterRequest register = memberFixture.registerRequest("name", "email@email.com", "password", role);
        ApiHelper.post(ApiHelper.MEMBER_ENDPOINT, register);
        LoginRequest login = memberFixture.loginRequest("email@email.com", "password");
        return ApiHelper.post("/login", login)
                .then().log().all()
                .extract().cookie("token");
    }
}
