package roomescape.controller;

import static org.hamcrest.Matchers.containsString;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.infra.auth.JwtTokenProcessor;
import roomescape.model.user.Role;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ReservationControllerTest {

    @Autowired
    private JwtTokenProcessor jwtTokenProcessor;

    private String adminToken;
    private String userToken;

    private final String loginToken = "loginToken";

    @BeforeEach
    void setUp() {
        adminToken = jwtTokenProcessor.createToken("asd@asd.com", Role.ADMIN);
        userToken = jwtTokenProcessor.createToken("vec@vec.com", Role.USER);
    }

    void Test_ReservationTime_Post() {
        Map<String, String> params = new HashMap<>();
        params.put("startAt", "10:00");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/times")
                .then().log().all()
                .statusCode(201);
    }

    private static void Test_Theme_Post() {
        Map<String, String> params = new HashMap<>();
        params.put("name", "Ddyong");
        params.put("description", "살인마가 쫓아오는 느낌");
        params.put("thumbnail", "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/themes")
                .then().log().all()
                .statusCode(201);
    }

    @Test
    @DisplayName("필드값 null 검증")
    void test1() {
        Map<String, Object> params = new HashMap<>();
        params.put("date", null);
        params.put("timeId", 1);
        params.put("themeId", 1);

        RestAssured.given().log().all()
                .cookie(loginToken, userToken)
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(400).body(containsString("[ERROR]"))
        ;
    }

    @Test
    @DisplayName("과거 예약을 생성하면 예외 처리한다. - 1일 전")
    void test2() {
        Test_ReservationTime_Post();
        Test_Theme_Post();

        Map<String, Object> params = new HashMap<>();
        params.put("date", String.valueOf(LocalDate.now().minusDays(1)));
        params.put("timeId", 1);
        params.put("themeId", 1);

        RestAssured.given().log().all()
                .cookie(loginToken, userToken)
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(400).body(containsString("[ERROR]"))
        ;
    }

    @Test
    @DisplayName("중복 예약을 생성하면 예외 처리한다.")
    void test4() {
        Test_ReservationTime_Post();
        Test_Theme_Post();

        Map<String, Object> params = new HashMap<>();
        params.put("date", "2222-02-02");
        params.put("timeId", 1);
        params.put("themeId", 1);

        RestAssured.given().log().all()
                .cookie(loginToken, userToken)
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(201);

        params.replace("name", "벡터");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .cookie(loginToken, userToken)
                .body(params)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(400)
                .body(containsString("[ERROR]"))
        ;
    }

}
