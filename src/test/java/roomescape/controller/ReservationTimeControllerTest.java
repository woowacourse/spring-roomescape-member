package roomescape.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.IntStream;
import org.hamcrest.core.IsNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.jdbc.Sql;
import roomescape.application.ReservationTimeService;
import roomescape.domain.time.ReservationTime;
import roomescape.dto.reservationtime.ReservationTimeRequest;

@DisplayName("예약 시간 컨트롤러")
@ExtendWith(MockitoExtension.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@Sql("/clear.sql")
class ReservationTimeControllerTest {

    @MockBean
    private ReservationTimeService reservationTimeService;

    @Autowired
    private ObjectMapper objectMapper;

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Test
    void 예약_시간을_생성한다() throws Exception {
        ReservationTime reservationTime = new ReservationTime(1L, LocalTime.parse("11:23"));
        ReservationTimeRequest request = new ReservationTimeRequest(reservationTime.getStartAt());
        when(reservationTimeService.register(request)).thenReturn(reservationTime);
        String content = objectMapper.writeValueAsString(request);

        RestAssured.given()
                .contentType(ContentType.JSON).log().all()
                .body(content)
                .when().post("/times")
                .then()
                .statusCode(201)
                .body("id", equalTo(Integer.parseInt(reservationTime.getId().toString())))
                .body("startAt", equalTo(reservationTime.getStartAt().toString()));
    }

    @Test
    void 전체_예약_시간을_조회한다() {
        List<ReservationTime> reservationTimes = IntStream.range(0, 3)
                .mapToObj(i -> new ReservationTime(1L, LocalTime.parse("11:23")))
                .toList();
        Mockito.when(reservationTimeService.getReservationTimes())
                .thenReturn(reservationTimes);

        ExtractableResponse<Response> response = RestAssured.given()
                .when().get("/times")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .extract();

        JsonPath result = response.jsonPath();

        assertAll(
                () -> assertThat(result.getList("$")).hasSize(3),
                () -> assertEquals(result.getString("[0].id"), reservationTimes.get(0).getId().toString()),
                () -> assertEquals(result.getString("[0].startAt"), reservationTimes.get(0).getStartAt().toString())
        );
    }

    @Test
    void 예약_시간을_삭제한다() {
        long id = 1L;
        doNothing().when(reservationTimeService).delete(id);

        RestAssured.given()
                .when().delete("/times/" + id)
                .then().statusCode(204);

    }

    @Test
    void post시_예약_시간이_비어있으면_Bad_Request_상태를_반환한다() {
        String content = "{}";

        RestAssured.given()
                .contentType(ContentType.JSON)
                .when().body(content)
                .then().statusCode(400)
                .body("$.fieldErrors[0].field", equalTo("startAt"))
                .body("$.fieldErrors[0].rejectedValue", equalTo(IsNull.nullValue()))
                .body("$.fieldErrors[0].reason", equalTo("시작 시간은 필수입니다."));
    }

    @Test
    void 예약_시간이_포맷에_맞지_않을_경우_Bad_Request_상태를_반환한다() {
        String content = "{\"startAt\":\"1112\"}";

        RestAssured.given()
                .contentType(ContentType.JSON)
                .when().body(content)
                .then().statusCode(400)
                .body("$.message", equalTo("잘못된 데이터 형식입니다."));
    }

    @Test
    void 예약_시간이_올바르지_않을_경우_Bad_Request_상태를_반환한다() throws Exception {
        String content = "{\"startAt\":\"14:89\"}";

        RestAssured.given()
                .contentType(ContentType.JSON)
                .when().body(content)
                .then().statusCode(400)
                .body("$.message", equalTo("잘못된 데이터 형식입니다."));
    }
}
