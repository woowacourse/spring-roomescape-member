package roomescape.domain.theme.controller;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.is;

import io.restassured.RestAssured;
import java.time.Clock;
import java.time.LocalDate;
import java.time.ZoneId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ThemeControllerTest {

    @TestConfiguration
    static class FixedClockConfig {

        @Bean
        @Primary
        Clock fixedClock() {
            return Clock.fixed(
                    LocalDate.of(2026, 5, 6)
                            .atStartOfDay(ZoneId.systemDefault())
                            .toInstant(),
                    ZoneId.systemDefault()
            );
        }
    }

    @LocalServerPort
    int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Test
    @DisplayName("테마 목록을 조회한다.")
    void getAllThemes() {
        RestAssured.given().log().all()
                .when().get("/themes")
                .then().log().all()
                .statusCode(200)
                .body("themes.size()", is(10))
                .body("themes[0].id", is(1))
                .body("themes[0].name", is("워너비"))
                .body("themes[0].description", is("워너비 테마입니다."))
                .body("themes[0].thumbnailUrl", is("https://example.com/wannabe.png"));
    }

    @Test
    @DisplayName("특정 테마의 예약 가능 시간을 조회한다.")
    void getAllThemeReservationTimes() {
        RestAssured.given().log().all()
                .queryParam("date", "2026-05-05")
                .when().get("/themes/1/times")
                .then().log().all()
                .statusCode(200)
                .body("times.size()", is(6))
                .body("times.id", contains(1, 2, 3, 4, 5, 6))
                .body("times.startAt", contains(
                        "10:00",
                        "11:00",
                        "12:00",
                        "13:00",
                        "14:00",
                        "15:00"
                ))
                .body("times.isAvailable", contains(
                        false,
                        false,
                        false,
                        false,
                        false,
                        true
                ));
    }

    @Test
    @DisplayName("인기 테마 목록을 조회한다.")
    void getPopularThemes() {
        RestAssured.given().log().all()
                .queryParam("period", 7)
                .queryParam("limit", 2)
                .when().get("/themes/popular")
                .then().log().all()
                .statusCode(200)
                .body("popularThemes.size()", is(2))
                .body("popularThemes[0].id", is(1))
                .body("popularThemes[0].name", is("워너비"))
                .body("popularThemes[0].description", is("워너비 테마입니다."))
                .body("popularThemes[0].thumbnailUrl", is("https://example.com/wannabe.png"))
                .body("popularThemes[0].rank", is(1))
                .body("popularThemes[1].id", is(2))
                .body("popularThemes[1].name", is("공포의 지하실"))
                .body("popularThemes[1].description", is("지하실에서 탈출하세요."))
                .body("popularThemes[1].thumbnailUrl", is("https://example.com/basement.png"))
                .body("popularThemes[1].rank", is(2));
    }
}
