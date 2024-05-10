package roomescape.reservation.controller;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import roomescape.auth.token.TokenProvider;
import roomescape.member.model.MemberRole;
import roomescape.reservation.dto.SaveThemeRequest;
import roomescape.reservation.dto.ThemeResponse;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@Sql(value = {"/schema.sql", "/data.sql"}, executionPhase = BEFORE_TEST_METHOD)
class ThemeControllerTest {

    @Autowired
    private TokenProvider tokenProvider;

    @LocalServerPort
    int randomServerPort;

    @BeforeEach
    public void initReservation() {
        RestAssured.port = randomServerPort;
    }

    @DisplayName("전체 테마 정보를 조회한다.")
    @Test
    void getThemesTest() {
        RestAssured.given().log().all()
                .when().get("/themes")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(15));
    }

    @DisplayName("테마 정보를 저장한다.")
    @Test
    void saveThemeTest() {
        final SaveThemeRequest saveThemeRequest = new SaveThemeRequest(
                "즐거운 방방탈출~",
                "방방방! 탈탈탈!",
                "방방 사진"
        );

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .cookie("token", createAdminAccessToken())
                .body(saveThemeRequest)
                .when().post("/admin/themes")
                .then().log().all()
                .statusCode(201)
                .body("id", is(16));
    }

    @DisplayName("관리자가 아닌 클라이언트가 테마 정보를 저장하려고 하면 예외를 발생시킨다.")
    @Test
    void saveThemeWhoNotAdminTest() {
        final SaveThemeRequest saveThemeRequest = new SaveThemeRequest(
                "즐거운 방방탈출~",
                "방방방! 탈탈탈!",
                "방방 사진"
        );

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .cookie("token", createUserAccessToken())
                .body(saveThemeRequest)
                .when().post("/admin/themes")
                .then().log().all()
                .statusCode(403)
                .body("message", is("유효하지 않은 권한 요청입니다."));
    }

    @DisplayName("테마 정보를 삭제한다.")
    @Test
    void deleteThemeTest() {
        // 예약 시간 정보 삭제
        RestAssured.given().log().all()
                .cookie("token", createAdminAccessToken())
                .when().delete("/admin/themes/7")
                .then().log().all()
                .statusCode(204);

        // 예약 시간 정보 조회
        final List<ThemeResponse> themes = RestAssured.given().log().all()
                .when().get("/themes")
                .then().log().all()
                .statusCode(200).extract()
                .jsonPath().getList(".", ThemeResponse.class);

        assertThat(themes.size()).isEqualTo(14);
    }

    @DisplayName("관리자가 아닌 클라이언트가 테마 정보를 삭제하려고 하면 예외를 발생시킨다.")
    @Test
    void deleteThemeWhoNotAdminTest() {
        // 예약 시간 정보 삭제
        RestAssured.given().log().all()
                .cookie("token", createUserAccessToken())
                .when().delete("/admin/themes/7")
                .then().log().all()
                .statusCode(403)
                .body("message", is("유효하지 않은 권한 요청입니다."));
    }

    private String createUserAccessToken() {
        return tokenProvider.createToken(
                3L,
                MemberRole.USER
        ).getValue();
    }

    private String createAdminAccessToken() {
        return tokenProvider.createToken(
                1L,
                MemberRole.ADMIN
        ).getValue();
    }
}
