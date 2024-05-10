package roomescape.controller;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import roomescape.domain.Member;
import roomescape.domain.Role;
import roomescape.domain.repository.MemberRepository;
import roomescape.dto.request.LoginRequest;

class PageControllerTest extends BaseControllerTest {

    @Autowired
    MemberRepository memberRepository;

    @BeforeEach
    void setUp() {
        memberRepository.save(new Member("admin@naver.com", "admin", "어드민", Role.ADMIN));
    }

    @Test
    @DisplayName("메인 페이지를 조회한다.")
    void mainPage() {
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .when().get("/")
                .then().log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    @DisplayName("예약 페이지를 조회한다.")
    void reservationPage() {
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .when().get("/reservation")
                .then().log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    @DisplayName("어드민 페이지를 조회한다.")
    void adminPage() {
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .cookie("token", getToken("admin@naver.com", "admin"))
                .when().get("/admin")
                .then().log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    @DisplayName("어드민 예약 페이지를 조회한다.")
    void adminReservationPage() {
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .cookie("token", getToken("admin@naver.com", "admin"))
                .when().get("/admin/reservation")
                .then().log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    @DisplayName("어드민 예약 시간 페이지를 조회한다.")
    void adminTimePage() {
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .cookie("token", getToken("admin@naver.com", "admin"))
                .when().get("/admin/time")
                .then().log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    @DisplayName("어드민 테마 페이지를 조회한다.")
    void adminThemePage() {
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .cookie("token", getToken("admin@naver.com", "admin"))
                .when().get("/admin/theme")
                .then().log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private String getToken(String email, String password) {
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(new LoginRequest(email, password))
                .when().post("/login")
                .then().log().all()
                .extract();

        return response.cookie("token");
    }
}
