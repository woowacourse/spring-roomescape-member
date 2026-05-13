package roomescape.controller.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import io.restassured.common.mapper.TypeRef;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.web.context.WebApplicationContext;
import roomescape.controller.BaseControllerUnitTest;
import roomescape.controller.fixture.ReservationRequestFixture;
import roomescape.domain.Reservation;
import roomescape.domain.fixture.ReservationFixture;
import roomescape.service.ReservationService;
import roomescape.web.controller.user.ReservationController;
import roomescape.web.dto.reservation.ReservationRequest;
import roomescape.web.dto.reservation.ReservationResponse;
import roomescape.web.dto.reservation.ReservationResponses;
import roomescape.web.dto.reservationTime.ReservationTimeResponse;
import roomescape.web.dto.theme.ThemeResponse;

@WebMvcTest(ReservationController.class)
class ReservationControllerTest extends BaseControllerUnitTest {

    @MockitoBean
    private ReservationService reservationService;

    @BeforeEach
    void setUp(WebApplicationContext webApplicationContext) {
        mockMvcSetting(webApplicationContext);
    }

    @ParameterizedTest(name = "요청 정보가 {0} 일 때, 예외 메세지 \"{1}\"가 발생한다.")
    @MethodSource("roomescape.controller.fixture.ReservationRequestFixture#reserveFailRequestFixture")
    void 예약_요청_시_형식_검증에_실패하면_예외가_발생한다(ReservationRequest body, String exceptionMessage) {
        // given: 실패하는 request body가 주어짐
        // when & then
        RestAssuredMockMvc.given().spec(defaultSpec()).log().all()
                .body(body)
                .when().post("/api/reservations")
                .then().log().all()
                .status(HttpStatus.BAD_REQUEST)
                .body(containsString(exceptionMessage));
    }

    @Test
    void 예약_요청에_성공하면_201_Created_상태와_정상_응답이_반환된다() {
        // given
        ReservationRequest request = ReservationRequestFixture.reserveSuccessRequestFixture();
        ReservationTimeResponse timeResponse = new ReservationTimeResponse(1L, LocalTime.now());
        ThemeResponse themeResponse = new ThemeResponse(1L, "바니의 집", "바니의 테마입니다.", "http://image.png.image.com");

        ReservationResponse expected = new ReservationResponse(1L, "이프", LocalDate.now(), timeResponse, themeResponse);
        when(reservationService.reserve(any(ReservationRequest.class))).thenReturn(expected);

        // when & then
        ReservationResponse response = RestAssuredMockMvc.given().spec(defaultSpec()).log().all()
                .body(request)
                .when().post("/api/reservations")
                .then().log().all()
                .status(HttpStatus.CREATED)
                .header("Location", containsString("/api/reservations/1"))
                .extract().as(new TypeRef<>() {
                });

        assertThat(response).isEqualTo(expected);
    }

    @Test
    void 예약_목록_조회_요청에_성공하면_200_OK와_예약_목록이_반환된다() {
        // given
        Reservation reservation = ReservationFixture.createDefaultReservationWithName("웨지");
        ReservationResponses expected = new ReservationResponses(List.of(ReservationResponse.from(reservation)));
        when(reservationService.getReservationsByUser(any(String.class))).thenReturn(expected.responses());

        // when & then
        ReservationResponses response = RestAssuredMockMvc.given().spec(defaultSpec()).log().all()
                .queryParam("name", "웨지")
                .when().get("/api/reservations")
                .then().log().all()
                .status(HttpStatus.OK)
                .extract().as(new TypeRef<>() {
                });

        assertThat(response).isEqualTo(expected);
    }
}
