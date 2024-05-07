package roomescape.controller;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import roomescape.service.dto.ReservationRequest;
import roomescape.service.dto.ReservationTimeCreateRequest;
import roomescape.service.dto.ThemeRequest;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class ThemeControllerTest {
    @LocalServerPort
    private int port;

    @BeforeEach
    void initPort() {
        RestAssured.port = port;
    }

    @AfterEach
    void initData() {
        RestAssured.get("/reservations")
                .then().extract().body().jsonPath().getList("id")
                .forEach(id -> RestAssured.delete("/reservations/" + id));

        RestAssured.get("/times")
                .then().extract().body().jsonPath().getList("id")
                .forEach(id -> RestAssured.delete("/times/" + id));

        RestAssured.get("/themes")
                .then().extract().body().jsonPath().getList("id")
                .forEach(id -> RestAssured.delete("/themes/" + id));
    }

    @DisplayName("테마 추가 성공 테스트")
    @Test
    void createTheme() {
        //given
        ThemeRequest themeRequest = new ThemeRequest("레벨2 탈출", "우테코 레벨2를 탈출하는 내용입니다.",
                "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg");

        //when&then
        RestAssured.given().contentType(ContentType.JSON).body(themeRequest)
                .when().post("/themes")
                .then().log().all().statusCode(201).body("id", is(greaterThan(0)));
    }

    @DisplayName("테마 추가 실패 테스트 - 이름 오류")
    @Test
    void cannotCreateThemeByName() {
        //given
        ThemeRequest themeRequest = new ThemeRequest("", "우테코 레벨2를 탈출하는 내용입니다.",
                "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg");

        //when&then
        RestAssured.given().contentType(ContentType.JSON).body(themeRequest)
                .when().post("/themes")
                .then().log().all().statusCode(400).body("message", is("이름은 빈칸(공백)일 수 없습니다."));
    }

    @DisplayName("테마 추가 실패 테스트 - 썸네일 형식 오류")
    @Test
    void cannotCreateThemeByThumbnail() {
        //given
        ThemeRequest themeRequest = new ThemeRequest("lily", "우테코 레벨2를 탈출하는 내용입니다.",
                "//i.pinimg.com/236x/6e/bc/4");

        //when&then
        RestAssured.given().contentType(ContentType.JSON).body(themeRequest)
                .when().post("/themes")
                .then().log().all().statusCode(400).body("message", is("올바르지 않은 썸네일 형식입니다."));
    }

    @DisplayName("테마 조회 성공 테스트")
    @Test
    void findAll() {
        //given
        ThemeRequest themeRequest = new ThemeRequest("레벨2 탈출", "우테코 레벨2를 탈출하는 내용입니다.",
                "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg");
        RestAssured.given().contentType(ContentType.JSON).body(themeRequest)
                .when().post("/themes");

        //when&then
        RestAssured.given().log().all()
                .when().get("/themes")
                .then().log().all().statusCode(200).body("size()", is(1));
    }

    @DisplayName("테마 삭제 성공 테스트")
    @Test
    void deleteTheme() {
        //given
        ThemeRequest themeRequest = new ThemeRequest("레벨2 탈출", "우테코 레벨2를 탈출하는 내용입니다.",
                "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg");
        long id = ((Number) RestAssured.given().contentType(ContentType.JSON).body(themeRequest)
                .when().post("/themes")
                .then().extract().body().jsonPath().get("id")).longValue();

        //when&then
        RestAssured.given().log().all()
                .when().delete("/themes/" + id)
                .then().log().all().statusCode(204);

        RestAssured.given().log().all()
                .when().get("/themes")
                .then().log().all()
                .assertThat().body("size()", is(0));
    }

    //TODO: 현재보다 이전으로 일정 설정 불가 검증으로 인한 테스트 불가
    @Disabled
    @DisplayName("인기 테마 조회 성공 테스트")
    @Test
    void findPopularThemes() {
        //given
        long timeId = ((Number) RestAssured.given().contentType(ContentType.JSON)
                .body(new ReservationTimeCreateRequest("10:00"))
                .when().post("/times")
                .then().extract().response().jsonPath().get("id")).longValue();

        long theme1Id = createThemeByName("레벨1 탈출");
        long theme2Id = createThemeByName("레벨2 탈출");
        long theme3Id = createThemeByName("레벨3 탈출");

        createReservation(timeId, theme1Id, LocalDate.now().minusDays(1).format(DateTimeFormatter.ISO_DATE));
        createReservation(timeId, theme2Id, LocalDate.now().minusDays(2).format(DateTimeFormatter.ISO_DATE));
        createReservation(timeId, theme3Id, LocalDate.now().minusDays(8).format(DateTimeFormatter.ISO_DATE));

        //when&then
        RestAssured.given().log().all()
                .when().get("/themes/popular")
                .then().log().all().statusCode(200).body("size()", is(2));
    }

    private void createReservation(long timeId, long themeId, String date) {
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(new ReservationRequest("브라운", date, timeId, themeId))
                .when().post("/reservations");
    }

    private long createThemeByName(String name) {
        ThemeRequest themeRequest = new ThemeRequest(name, "우테코 레벨2를 탈출하는 내용입니다.",
                "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg");
        long themeId = ((Number) RestAssured.given().contentType(ContentType.JSON).body(themeRequest)
                .when().post("/themes")
                .then().extract().response().jsonPath().get("id")).longValue();
        return themeId;
    }
}
