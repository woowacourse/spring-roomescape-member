package roomescape.member.presentation;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.util.Map;
import roomescape.member.presentation.dto.SignUpRequest;
import roomescape.member.presentation.dto.TokenRequest;

public class MemberFixture {

    private static final String USER_EMAIL = "user@user.com";
    private static final String USER_PASSWORD = "user";
    private static final String USER_NAME = "유저";
    private static final String ADMIN_EMAIL = "admin@admin.com";
    private static final String ADMIN_PASSWORD = "admin";
    private static final String ADMIN = "어드민";

    public SignUpRequest createMemberRequest(String email, String password, String name) {
        return new SignUpRequest(email, password, name);
    }

    public TokenRequest createLoginRequest(String email, String password) {
        return new TokenRequest(email, password);
    }

    public void createMember(String email, String password, String name) {
        final SignUpRequest reservationRequest = createMemberRequest(email, password, name);
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(reservationRequest)
                .when().post("/signUp");
    }

    public void createUser() {
        final SignUpRequest reservationRequest = createMemberRequest(USER_EMAIL, USER_PASSWORD, USER_NAME);
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(reservationRequest)
                .when().post("/signUp");
    }

    public void createAdmin() {
        final SignUpRequest reservationRequest = createMemberRequest(ADMIN_EMAIL, ADMIN_PASSWORD, ADMIN);
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(reservationRequest)
                .when().post("/signUp");
    }

    public Map<String, String> loginUser() {
        createUser();
        TokenRequest tokenRequest = createLoginRequest(USER_EMAIL, USER_PASSWORD);

        return RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(tokenRequest)
                .when().post("/login")
                .then().log().all()
                .extract().cookies();
    }

}
