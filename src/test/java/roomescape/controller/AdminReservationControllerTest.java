package roomescape.controller;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.dto.request.ReservationRequest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class AdminReservationControllerTest {
    private String token;

    @BeforeEach
    void setUp() {
        ApiTestFixture.signUpAdmin("admin@naver.com", "password", "admin");
        ApiTestFixture.signUpUser("abc@naver.com", "password", "vector");

        LocalTime futureTime = LocalTime.now().plusHours(1);
        ApiTestFixture.createReservationTime(futureTime);
        ApiTestFixture.createTheme("테마1", "설명1", "https://thumb");

        token = ApiTestFixture.loginAndGetToken("admin@naver.com", "password");
    }

    @Test
    @DisplayName("예약을 생성하면 201을 반환한다.")
    void createReservationTest() {
        ReservationRequest req = new ReservationRequest(
                1L, LocalDate.of(2222, 2, 2), 1L, 1L
        );

        ApiTestHelper.post("/admin/reservations", token, req)
                .statusCode(201);
    }

    @Test
    @DisplayName("필드값 null 을 넣으면 400 에러를 반환한다.")
    void createReservation_withNullFieldTest() {
        Map<String, Object> body = new HashMap<>();
        body.put("memberId", 1);
        body.put("date", "2222-05-05");
        body.put("timeId", null);
        body.put("themeId", 1);

        ApiTestHelper.post("/admin/reservations", token, body)
                .statusCode(400)
                .body(containsString("[ERROR]"));
    }

    @Test
    @DisplayName("과거 예약을 생성하면 예외 처리한다. - 1일 전")
    void createReservation_pastDateDayTest() {
        ReservationRequest req = new ReservationRequest(
                1L, LocalDate.now().minusDays(1), 1L, 1L
        );

        ApiTestHelper.post("/admin/reservations", token, req)
                .statusCode(400)
                .body(containsString("[ERROR] 과거 예약은 불가능합니다."));
    }

    @Test
    @DisplayName("과거 예약을 생성하면 예외 처리한다. - 1시간 전")
    void createReservation_pastTime() {
        LocalTime pastTime = LocalTime.now().minusHours(1);
        ApiTestFixture.createReservationTime(pastTime);

        ReservationRequest req = new ReservationRequest(
                1L, LocalDate.now(), 2L, 1L
        );

        ApiTestHelper.post("/admin/reservations", token, req)
                .statusCode(400)
                .body(containsString("[ERROR] 과거 예약은 불가능합니다."));
    }

    @Test
    @DisplayName("중복 예약을 생성하면 예외 처리한다.")
    void createReservation_duplicate() {
        ReservationRequest req = new ReservationRequest(
                1L, LocalDate.of(2222, 2, 2), 1L, 1L
        );

        ApiTestHelper.post("/admin/reservations", token, req)
                .statusCode(201);

        ApiTestHelper.post("/admin/reservations", token, req)
                .statusCode(400)
                .body(containsString("[ERROR] Reservation already exists"));
    }

    @Test
    @DisplayName("조건 없이 조회하면 전체 예약 리스트를 반환한다.")
    void getAllReservations() {
        ReservationRequest r1 = new ReservationRequest(
                1L, LocalDate.of(2222, 2, 2), 1L, 1L
        );
        ReservationRequest r2 = new ReservationRequest(
                1L, LocalDate.of(2222, 2, 3), 1L, 1L
        );

        ApiTestHelper.post("/admin/reservations", token, r1).statusCode(201);
        ApiTestHelper.post("/admin/reservations", token, r2).statusCode(201);

        ApiTestHelper.get("/admin/reservations", token)
                .statusCode(200)
                .body("size()", is(2));
    }

    @Test
    @DisplayName("3건의 예약 중 쿼리 파라미터로 필터링된 예약 하나만 조회한다.")
    void getFilteredReservations() {
        ReservationRequest r1 = new ReservationRequest(
                1L, LocalDate.of(2222, 2, 2), 1L, 1L
        );
        ReservationRequest r2 = new ReservationRequest(
                2L, LocalDate.of(2222, 2, 3), 1L, 1L
        );
        ReservationRequest r3 = new ReservationRequest(
                1L, LocalDate.of(2222, 3, 1), 1L, 1L
        );

        ApiTestHelper.post("/admin/reservations", token, r1).statusCode(201);
        ApiTestHelper.post("/admin/reservations", token, r2).statusCode(201);
        ApiTestHelper.post("/admin/reservations", token, r3).statusCode(201);

        ApiTestHelper.get("/admin/reservations?memberId=1&themeId=1&from=2222-02-01&to=2222-02-28", token)
                .statusCode(200)
                .body("size()", is(1));
    }

    @Test
    @DisplayName("예약 삭제 시 204를 반환한다.")
    void deleteReservation() {
        ReservationRequest req = new ReservationRequest(
                1L, LocalDate.of(2222, 2, 2), 1L, 1L
        );
        long id = ApiTestHelper.post("/admin/reservations", token, req)
                .statusCode(201)
                .extract().jsonPath().getLong("id");

        ApiTestHelper.delete("/admin/reservations/" + id, token)
                .statusCode(204);
    }
}
