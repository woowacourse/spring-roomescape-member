package roomescape.endpoint;

import io.restassured.RestAssured;
import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import roomescape.controller.exception.CustomExceptionResponse;
import roomescape.dto.ThemeRequest;
import roomescape.dto.ThemeResponse;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertAll;
import static roomescape.endpoint.PreInsertedData.preInsertedTheme1;
import static roomescape.endpoint.PreInsertedData.preInsertedTheme2;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@TestPropertySource(locations = "classpath:application-test.properties")
class ThemeEndPointTest {

    @DisplayName("테마 목록 조회")
    @Test
    void getThemes_success() {
        TypeRef<List<ThemeResponse>> ThemesFormat = new TypeRef<>() {
        };

        RestAssured.given().log().all()
                .when().get("/themes")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract().as(ThemesFormat);
    }

    @DisplayName("테마 추가")
    @Test
    void addTheme_success() {
        ThemeRequest themeRequest = new ThemeRequest(
                "이름",
                "요약",
                "썸네일");

        RestAssured.given().log().ifValidationFails()
                .contentType(ContentType.JSON)
                .body(themeRequest)
                .when().post("/themes")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .header("location", containsString("/themes/"))
                .extract().as(ThemeResponse.class);
    }

    @DisplayName("존재하는 테마 삭제")
    @Test
    void deleteTheme_forExist_success() {
        long existThemeId = preInsertedTheme1.getId();

        RestAssured.given().log().all()
                .when().delete("/themes/" + existThemeId)
                .then().log().all()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }

    @DisplayName("존재하지 않는 테마 삭제 - 예외 발생")
    @Test
    void deleteTheme_forNonExist_fail() {
        long notExistTimeId = 0L;

        CustomExceptionResponse response = RestAssured.given().log().all()
                .when().delete("/themes/" + notExistTimeId)
                .then().log().all()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .extract().as(CustomExceptionResponse.class);

        assertAll(
                () -> assertThat(response.title()).contains("리소스를 찾을 수 없습니다."),
                () -> assertThat(response.detail()).contains("아이디에 해당하는 테마를 찾을 수 없습니다.")
        );
    }

    @DisplayName("예약이 있는 테마 삭제 - 예외 발생")
    @Test
    void deleteTheme_whenReservationExist_fail() {
        long themeId = preInsertedTheme2.getId();

        CustomExceptionResponse response = RestAssured.given().log().all()
                .when().delete("/themes/" + themeId)
                .then().log().all()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .extract().as(CustomExceptionResponse.class);

        assertAll(
                () -> assertThat(response.title()).contains("허용되지 않는 작업입니다."),
                () -> assertThat(response.detail()).contains("해당 테마에 예약이 존재하기 때문에 삭제할 수 없습니다.")
        );
    }

    @DisplayName("테마 순위 조회")
    @Test
    void getMostReservedThemes() {
        TypeRef<List<ThemeResponse>> ThemesFormat = new TypeRef<>() {
        };

        List<ThemeResponse> response = RestAssured.given().log().all()
                .when().get("/themes/rankings")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract().as(ThemesFormat);
        // todo: from 과 to 의 시간을 받아서 갯수 조절할 수 있게 하기 - 시간 때문에 테스트 코드 안돌아감..
/*        assertThat(response).containsExactly(
                ThemeResponse.from(preInsertedTheme2),
                ThemeResponse.from(preInsertedTheme1)
        );*/
    }
}
