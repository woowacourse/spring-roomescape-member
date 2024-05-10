package roomescape.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import roomescape.domain.Member;
import roomescape.domain.Role;
import roomescape.domain.repository.MemberRepository;
import roomescape.dto.request.LoginRequest;
import roomescape.dto.response.TokenResponse;
import roomescape.service.AuthService;

class AuthControllerTest extends BaseControllerTest {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    AuthService authService;

    @Test
    @DisplayName("로그인을 한다.")
    void login() {
        memberRepository.save(new Member("test123@example.com", "password", "test", Role.NORMAL));

        LoginRequest request = new LoginRequest("test123@example.com", "password");

        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(request)
                .when().post("/login")
                .then().log().all()
                .extract();

        assertAll(
                () -> assertThat(response.header("Set-Cookie")).contains("token="),
                () -> assertThat(response.header("Set-Cookie")).contains("Path=/"),
                () -> assertThat(response.header("Set-Cookie")).contains("HttpOnly")
        );
    }

    @Test
    @DisplayName("로그인 여부를 확인한다.")
    void check() {
        memberRepository.save(new Member("test123@example.com", "password", "test", Role.NORMAL));
        TokenResponse tokenResponse = authService.createToken(new LoginRequest("test123@example.com", "password"));
        String token = tokenResponse.accessToken();

        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .header("cookie", "token=" + token)
                .when().get("/login/check")
                .then().log().all()
                .extract();

        assertAll(
                () -> assertThat(response.header("Transfer-Encoding")).contains("chunked"),
                () -> assertThat(response.body().asString()).contains("test")
        );
    }
//
//    @TestFactory
//    @DisplayName("회원가입 후 로그인을 한다.")
//    Stream<DynamicTest> addMemberAndGetAndDelete() {
//        return Stream.of(
//                DynamicTest.dynamicTest("사용자를 생성한다.", this::addMember),
//                DynamicTest.dynamicTest("로그인을 한다.", this::getAllMembers),
//        );
//    }
//
//    @TestFactory
//    @DisplayName("중복된 email의 사용자를 생성하면 실패한다.")
//    Stream<DynamicTest> failWhenDuplicatedEmail() {
//        return Stream.of(
//                DynamicTest.dynamicTest("사용자를 생성한다.", this::addMember),
//                DynamicTest.dynamicTest("이미 존재하는 email의 사용자를 생성한다.", this::addMemberFailWhenDuplicatedEmail)
//        );
//    }
//
//    @Test
//    @DisplayName("존재하지 않는 사용자를 삭제하면 실패한다.")
//    void deleteMemberByIdFailWhenNotFoundId() {
//        ExtractableResponse<Response> response = RestAssured.given().log().all()
//                .when().delete("/members/1")
//                .then().log().all()
//                .extract();
//
//        SoftAssertions.assertSoftly(softly -> {
//            softly.assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
//            softly.assertThat(response.body().asString()).contains("해당 id의 사용자가 존재하지 않습니다.");
//        });
//    }
//
//    private void addMember() {
//        MemberRequest request = new MemberRequest("email@email.com", "password", "사용자");
//
//        ExtractableResponse<Response> response = RestAssured.given().log().all()
//                .contentType(ContentType.JSON)
//                .body(request)
//                .when().post("/members")
//                .then().log().all()
//                .extract();
//
//        MemberResponse reservationMemberResponse = response.as(MemberResponse.class);
//
//        SoftAssertions.assertSoftly(softly -> {
//            softly.assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
//            softly.assertThat(response.header("Location")).isEqualTo("/members/1");
//            softly.assertThat(reservationMemberResponse).isEqualTo(new MemberResponse(1L, "email@email.com", "사용자"));
//        });
//    }
//
//    private void addMemberFailWhenDuplicatedEmail() {
//        MemberRequest request = new MemberRequest("email@email.com", "password", "사용자");
//
//        ExtractableResponse<Response> response = RestAssured.given().log().all()
//                .contentType(ContentType.JSON)
//                .body(request)
//                .when().post("/members")
//                .then().log().all()
//                .extract();
//
//        SoftAssertions.assertSoftly(softly -> {
//            softly.assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
//            softly.assertThat(response.body().asString()).contains("해당 email의 사용자가 이미 존재합니다.");
//        });
//    }
//
//    private void getAllMembers() {
//        ExtractableResponse<Response> response = RestAssured.given().log().all()
//                .when().get("/members")
//                .then().log().all()
//                .extract();
//
//        List<MemberResponse> reservationMemberResponses = response.jsonPath()
//                .getList(".", MemberResponse.class);
//
//        SoftAssertions.assertSoftly(softly -> {
//            softly.assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
//            softly.assertThat(reservationMemberResponses).hasSize(1);
//            softly.assertThat(reservationMemberResponses)
//                    .containsExactly(new MemberResponse(1L, "email@email.com", "사용자"));
//        });
//    }
//
//    private void deleteMemberById() {
//        ExtractableResponse<Response> response = RestAssured.given().log().all()
//                .when().delete("/members/1")
//                .then().log().all()
//                .extract();
//
//        SoftAssertions.assertSoftly(softly -> {
//            softly.assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
//        });
//    }
}
