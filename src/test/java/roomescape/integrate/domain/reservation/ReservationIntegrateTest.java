package roomescape.integrate.domain.reservation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import roomescape.dto.reservation.ThemeResponseDto;
import roomescape.integrate.fixture.RequestFixture;

@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
class ReservationIntegrateTest {

    private final RequestFixture requestFixture = new RequestFixture();

    private static Map<String, String> cookies;
    private long themeId;
    private long timeId;

    @BeforeEach
    void setup() {
        requestFixture.reqeustSignup("투다", "test@email.com", "testtest");
        cookies = requestFixture.requestLogin("test@email.com", "testtest");
        themeId = requestFixture.requestAddTheme("테마 명", "description", "thumbnail");
        LocalTime afterTime = LocalTime.now().plusHours(1L);
        timeId = requestFixture.requestAddTime(afterTime.toString());
    }

    @Test
    void 예약_추가_테스트() {
        requestFixture.requestAddReservation("예약", LocalDate.now().toString(), themeId, timeId, cookies);

        RestAssured.given().log().all()
                .when().get("/reservations")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(1));
    }

    @Test
    void 예약_삭제_테스트() {
        long reservationId = requestFixture.requestAddReservation("예약", LocalDate.now().toString(), themeId, timeId,
                cookies);

        RestAssured.given().log().all()
                .when().get("/reservations")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(1));

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .cookies(cookies)
                .when().delete("/reservations/" + reservationId)
                .then().log().all()
                .statusCode(204);

        RestAssured.given().log().all()
                .when().get("/reservations")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(0));
    }

    @Test
    void 테마_랭킹_테스트() {
        LocalTime afterTime = LocalTime.now().plusHours(1);
        long timeId = requestFixture.requestAddTime(afterTime.toString());

        long themeId1 = requestFixture.requestAddTheme("테마 명1", "description", "thumbnail");
        long themeId2 = requestFixture.requestAddTheme("테마 명2", "description", "thumbnail");
        long themeId3 = requestFixture.requestAddTheme("테마 명3", "description", "thumbnail");

        requestFixture.requestAddReservation("테마1예약이름1", String.valueOf(LocalDate.now().minusDays(1)), timeId, themeId1,
                cookies);
        requestFixture.requestAddReservation("테마1예약이름2", String.valueOf(LocalDate.now().minusDays(2)), timeId, themeId1,
                cookies);
        requestFixture.requestAddReservation("테마1예약이름3", String.valueOf(LocalDate.now().minusDays(3)), timeId, themeId1,
                cookies);
        requestFixture.requestAddReservation("테마2예약이름1", String.valueOf(LocalDate.now().minusDays(1)), timeId, themeId2,
                cookies);
        requestFixture.requestAddReservation("테마2예약이름2", String.valueOf(LocalDate.now().minusDays(2)), timeId, themeId2,
                cookies);
        requestFixture.requestAddReservation("테마3예약이름1", String.valueOf(LocalDate.now().minusDays(1)), timeId, themeId3,
                cookies);

        Response response = RestAssured.given()
                .when().get("/reservations/popular-themes")
                .then()
                .extract().response();

        List<ThemeResponseDto> rankingThemes = response.jsonPath().getList("", ThemeResponseDto.class);
        List<Long> rankingThemeIds = rankingThemes.stream()
                .map(ThemeResponseDto::id)
                .toList();

        assertThat(rankingThemeIds).containsExactlyElementsOf(List.of(themeId1, themeId2, themeId3));
    }
}
