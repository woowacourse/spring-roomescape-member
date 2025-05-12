package roomescape.controller;

import static org.hamcrest.Matchers.containsString;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.dto.request.ReservationRequest;
import roomescape.dto.request.ReservationTimeRequest;
import roomescape.dto.request.ThemeRequest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ReservationTimeControllerTest {

    @Test
    @DisplayName("시작 시간 형식 검증")
    void addTime() {
        ApiTestHelper.post("/times", Map.of("startAt", "미친형식"))
                .statusCode(400);
    }

    @Test
    @DisplayName("시작 시간 null 검증")
    void addTime_null() {
        Map<String, String> param = new HashMap<>();
        param.put("startAt", null);
        ApiTestHelper.post("/times", "", param)
                .statusCode(400)
                .body(containsString("[ERROR] "));
    }

    @Test
    @DisplayName("예약된 시간을 삭제할 수 없음")
    void deleteTime_withExistingReservation() {
        ReservationTimeRequest timeReq = new ReservationTimeRequest(LocalTime.of(10, 0));
        long timeId = ApiTestHelper.post("/times", "", timeReq)
                .statusCode(201)
                .extract().jsonPath().getLong("id");

        ApiTestFixture.signUpAdmin("admin@naver.com", "password", "admin");
        String token = ApiTestFixture.loginAndGetToken("admin@naver.com", "password");

        ThemeRequest themeReq = new ThemeRequest(
                "Ddyong",
                "살인마가 쫓아오는 느낌",
                "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg"
        );
        long themeId = ApiTestHelper.post("/themes", token, themeReq)
                .statusCode(201)
                .extract().jsonPath().getLong("id");

        ReservationRequest resReq = new ReservationRequest(
                1L,
                LocalDate.of(2222, 2, 2),
                timeId,
                themeId
        );
        ApiTestHelper.post("/admin/reservations", token, resReq)
                .statusCode(201);

        ApiTestHelper.delete("/times/" + timeId)
                .statusCode(400);
    }
}
