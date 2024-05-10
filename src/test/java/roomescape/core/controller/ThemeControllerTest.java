package roomescape.core.controller;

import static org.hamcrest.Matchers.is;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import roomescape.core.dto.auth.TokenRequest;
import roomescape.core.dto.reservation.MemberReservationRequest;
import roomescape.core.dto.reservationtime.ReservationTimeRequest;
import roomescape.core.dto.theme.ThemeRequest;

@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@TestPropertySource(properties = {"spring.config.location = classpath:application-test.yml"})
class ThemeControllerTest {
    private static final String EMAIL = "test@email.com";
    private static final String PASSWORD = "password";

    private String accessToken;

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;

        accessToken = RestAssured
                .given().log().all()
                .body(new TokenRequest(EMAIL, PASSWORD))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/login")
                .then().log().cookies().extract().cookie("token");
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" "})
    @DisplayName("테마 생성 시, name 값이 올바르지 않으면 예외가 발생한다.")
    void validateThemeWithNameEmpty(final String name) {
        ThemeRequest request = new ThemeRequest(name, "우테코 레벨2를 탈출하는 내용입니다.",
                "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(request)
                .when().post("/themes")
                .then().log().all()
                .statusCode(400);
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" "})
    @DisplayName("테마 생성 시, description 값이 올바르지 않으면 예외가 발생한다.")
    void validateThemeWithDescriptionEmpty(final String description) {
        ThemeRequest request = new ThemeRequest("레벨2 탈출", description,
                "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(request)
                .when().post("/themes")
                .then().log().all()
                .statusCode(400);
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" "})
    @DisplayName("테마 생성 시, thumbnail 값이 올바르지 않으면 예외가 발생한다.")
    void validateThemeWithThumbnailEmpty(final String thumbnail) {
        ThemeRequest request = new ThemeRequest("레벨2 탈출", "우테코 레벨2를 탈출하는 내용입니다.", thumbnail);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(request)
                .when().post("/themes")
                .then().log().all()
                .statusCode(400);
    }

    @Test
    @DisplayName("테마 생성 시, name 값이 중복이면 예외가 발생한다.")
    void validateThemeWithDuplicatedName() {
        ThemeRequest request = new ThemeRequest("레벨2 탈출", "우테코 레벨2를 탈출하는 내용입니다.",
                "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(request)
                .when().post("/themes")
                .then().log().all()
                .statusCode(201);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(request)
                .when().post("/themes")
                .then().log().all()
                .statusCode(400);
    }

    @Test
    @DisplayName("모든 테마 목록을 조회한다.")
    void findAllThemes() {
        RestAssured.given().log().all()
                .when().get("/themes")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(5));
    }

    @Test
    @DisplayName("지난 한 주 동안의 인기 테마 목록을 조회한다.")
    void findPopularThemes() {
        createReservationTimes();
        createReservations();

        RestAssured.given().log().all()
                .when().get("/themes/popular")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(2))
                .body("name", is(List.of("테마2", "테마1")));
    }

    private void createReservationTimes() {
        ReservationTimeRequest timeRequest = new ReservationTimeRequest(
                LocalTime.now().plusMinutes(1).format(DateTimeFormatter.ofPattern("HH:mm")));

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(timeRequest)
                .when().post("/times")
                .then().log().all()
                .statusCode(201);

        ReservationTimeRequest timeRequest2 = new ReservationTimeRequest(
                LocalTime.now().plusMinutes(2).format(DateTimeFormatter.ofPattern("HH:mm")));

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(timeRequest2)
                .when().post("/times")
                .then().log().all()
                .statusCode(201);
    }

    private void createReservations() {
        MemberReservationRequest firstThemeMemberReservationRequest = new MemberReservationRequest(
                LocalDate.now().format(DateTimeFormatter.ISO_DATE),
                4L, 2L);

        RestAssured.given().log().all()
                .cookies("token", accessToken)
                .contentType(ContentType.JSON)
                .body(firstThemeMemberReservationRequest)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(201);

        MemberReservationRequest firstThemeMemberReservationRequest2 = new MemberReservationRequest(
                LocalDate.now().format(DateTimeFormatter.ISO_DATE),
                5L, 2L);

        RestAssured.given().log().all()
                .cookies("token", accessToken)
                .contentType(ContentType.JSON)
                .body(firstThemeMemberReservationRequest2)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(201);
    }

    @Test
    @DisplayName("테마 삭제 시, 해당 테마를 참조하는 예약이 있으면 예외가 발생한다.")
    void validateThemeDelete() {
        RestAssured.given().log().all()
                .when().delete("/themes/1")
                .then().log().all()
                .statusCode(400);
    }

    @Test
    @DisplayName("테마 삭제 시, 해당 테마를 참조하는 예약이 없으면 테마가 삭제된다.")
    void deleteTheme() {
        RestAssured.given().log().all()
                .when().delete("/themes/2")
                .then().log().all()
                .statusCode(204);
    }
}
