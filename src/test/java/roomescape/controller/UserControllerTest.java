package roomescape.controller;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import roomescape.dto.LoginRequestDto;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.is;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@Sql(scripts = {"/test_schema.sql", "/test_data.sql"})
public class UserControllerTest {

    @LocalServerPort
    int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @DisplayName("정상적인 예약 추가 요청 시 201으로 응답한다.")
    @Test
    void insertTest() {
        ZoneId kst = ZoneId.of("Asia/Seoul");
        Map<String, Object> params = new HashMap<>();

        params.put("date", LocalDate.now(kst).plusDays(2).toString());
        params.put("timeId", 1);
        params.put("themeId", 1);

        String accessToken = RestAssured
                .given().log().all()
                .body(new LoginRequestDto("email@email.com", "password"))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/login")
                .then().log().all().statusCode(200)
                .extract().header("Set-Cookie").split("=")[1];

        RestAssured.given().contentType("application/json").body(params).log().all()
                .cookie("token", accessToken)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(201);
    }

    @DisplayName("최근 일주일간 가장 예약이 많은 테마를 조회한다.")
    @Test
    void weeklyBestThemesTest() {
        RestAssured.given().log().all()
                .when().get("/best-themes")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(3));
    }
}
