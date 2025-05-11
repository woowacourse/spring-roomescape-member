package roomescape.controller;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static roomescape.controller.ApiTestFixture.createReservationTime;
import static roomescape.controller.ApiTestFixture.createTheme;
import static roomescape.controller.ApiTestFixture.loginAndGetToken;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ReservationControllerTest {
    private String token;

    @BeforeEach
    void setUp() {
        createReservationTime();
        createTheme();
        token = loginAndGetToken();
    }

    @Test
    @DisplayName("예약을 생성하면 201을 반환한다.")
    void test0() {

        Map<String, Object> params = new HashMap<>();
        params.put("memberId", 1);
        params.put("date", "2222-02-02");
        params.put("timeId", 1);
        params.put("themeId", 1);

        RestAssured.given().log().all()
                .cookie("token", token)
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/admin/reservations")
                .then().log().all()
                .statusCode(201);
        ;
    }

    @Test
    @DisplayName("필드값 null 검증")
    void test1() {

        Map<String, Object> params = new HashMap<>();
        params.put("memberId", null);
        params.put("date", "2025-08-05");
        params.put("timeId", 1);
        params.put("themeId", 1);

        RestAssured.given().log().all()
                .cookie("token", token)
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/admin/reservations")
                .then().log().all()
                .statusCode(400).body(containsString("[ERROR]"))
        ;
    }

    @Test
    @DisplayName("과거 예약을 생성하면 예외 처리한다. - 1일 전")
    void test2() {

        Map<String, Object> params = new HashMap<>();
        params.put("memberId", 1);
        params.put("date", String.valueOf(LocalDate.now().minusDays(1)));
        params.put("timeId", 1);
        params.put("themeId", 1);

        RestAssured.given().log().all()
                .cookie("token", token)
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/admin/reservations")
                .then().log().all()
                .statusCode(400).body(containsString("[ERROR] 과거 예약은 불가능합니다."))
        ;
    }

    @Test
    @DisplayName("과거 예약을 생성하면 예외 처리한다. - 1시간 전")
    void test3() {

        Map<String, String> timeParams = new HashMap<>();
        timeParams.put("startAt", LocalTime.now().minusHours(1).format(DateTimeFormatter.ofPattern("HH:mm")));

        RestAssured.given().log().all()
                .cookie("token", token)
                .contentType(ContentType.JSON)
                .body(timeParams)
                .when().post("/times")
                .then().log().all()
                .statusCode(201);

        Map<String, Object> reservationParams = new HashMap<>();
        reservationParams.put("memberId", 1);
        reservationParams.put("date", String.valueOf(LocalDate.now()));
        reservationParams.put("timeId", 1);
        reservationParams.put("themeId", 1);

        RestAssured.given().log().all()
                .cookie("token", token)
                .contentType(ContentType.JSON)
                .body(reservationParams)
                .when().post("/admin/reservations")
                .then().log().all()
                .statusCode(400).body(containsString("[ERROR] 과거 예약은 불가능합니다."))
        ;
    }

    @Test
    @DisplayName("중복 예약을 생성하면 예외 처리한다.")
    void test4() {

        Map<String, Object> params = new HashMap<>();
        params.put("memberId", 1);
        params.put("date", "2222-02-02");
        params.put("timeId", 1);
        params.put("themeId", 1);

        RestAssured.given().log().all()
                .cookie("token", token)
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/admin/reservations")
                .then().log().all()
                .statusCode(201);

        params.replace("name", "벡터");

        RestAssured.given().log().all()
                .cookie("token", token)
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/admin/reservations")
                .then().log().all()
                .statusCode(400)
                .body(containsString("[ERROR] Reservation already exists"))
        ;
    }

    //    @GetMapping()
//    public ResponseEntity<List<Reservation>> getReservations(
//            @RequestParam(value = "memberId", required = false) Long memberId,
//            @RequestParam(value = "themeId", required = false) Long themeId,
//            @RequestParam(value = "from", required = false) LocalDate fromDate,
//            @RequestParam(value = "to", required = false) LocalDate toDate
//    ) {
//        if (memberId == null || themeId == null || fromDate == null || toDate == null) {
//            return ResponseEntity.ok().body(reservationService.getAllReservations());
//        }
//        return ResponseEntity.ok()
//                .body(reservationService.getFilteredReservations(memberId, themeId, fromDate, toDate));
//    }
    @Test
    @DisplayName("조건 없이 조회하면 전체 예약 리스트를 반환한다.")
    void testGetAllReservations() {
        ApiTestFixture.createReservationTime();
        ApiTestFixture.createTheme();
        token = loginAndGetToken();
        Map<String, Object> params = new HashMap<>();
        params.put("memberId", 1);
        params.put("date", "2222-02-02");
        params.put("timeId", 1);
        params.put("themeId", 1);
        RestAssured.given().log().all()
                .cookie("token", token)
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/admin/reservations")
                .then().log().all()
                .statusCode(201);

        params.put("date", "2222-02-03");
        RestAssured.given().log().all()
                .cookie("token", token)
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/admin/reservations")
                .then().log().all()
                .statusCode(201);

        // GET 전체 조회
        RestAssured.given().log().all()
                .cookie("token", token)
                .when().get("/admin/reservations")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(2));
    }

    @Test
    @DisplayName("3건의 예약 중 쿼리 파라미터로 필터링된 예약 하나만 조회한다.")
    void testGetFilteredReservations() {
        // 예약 3건 생성: 2건은 검색 범위 밖, 1건만 범위 내
        ApiTestFixture.createReservationTime();
        ApiTestFixture.createTheme();
        token = loginAndGetToken();

        Map<String, Object> params = new HashMap<>();
        params.put("memberId", 1);
        params.put("timeId", 1);
        params.put("themeId", 1);

        params.put("date", "2222-02-02");
        RestAssured.given().log().all()
                .cookie("token", token)
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/admin/reservations")
                .then().statusCode(201);

        // 범위 밖 (memberId 다름)
        params.put("memberId", 2);
        params.put("date", "2222-02-03");
        RestAssured.given().log().all()
                .cookie("token", token)
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/admin/reservations")
                .then().statusCode(201);

        // 범위 밖 (date 다름)
        params.put("memberId", 1);
        params.put("date", "2222-03-01");
        RestAssured.given().log().all()
                .cookie("token", token)
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/admin/reservations")
                .then().statusCode(201);

        // GET 필터 조회
        RestAssured.given().log().all()
                .cookie("token", token)
                .queryParam("memberId", 1)
                .queryParam("themeId", 1)
                .queryParam("from", "2222-02-01")
                .queryParam("to", "2222-02-28")
                .when().get("/admin/reservations")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(1));
    }

}
