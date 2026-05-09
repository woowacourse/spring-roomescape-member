package roomescape.controller.admin;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import io.restassured.common.mapper.TypeRef;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.web.context.WebApplicationContext;
import roomescape.controller.BaseControllerUnitTest;
import roomescape.controller.fixture.ReservationTimeRequestFixture;
import roomescape.service.ReservationTimeService;
import roomescape.web.controller.admin.AdminReservationTimeController;
import roomescape.web.dto.ReservationTimeRequest;
import roomescape.web.dto.ReservationTimeResponse;
import roomescape.web.dto.ReservationTimeResponses;

@WebMvcTest(AdminReservationTimeController.class)
class AdminReservationTimeControllerTest extends BaseControllerUnitTest {

    @MockitoBean
    private ReservationTimeService reservationTimeService;

    @BeforeEach
    void setUp(WebApplicationContext webApplicationContext) {
        mockMvcSetting(webApplicationContext);
    }

    @ParameterizedTest(name = "요청 정보가 {0} 일 때, 예외 메세지 \"{1}\"가 발생한다.")
    @MethodSource("roomescape.controller.fixture.ReservationTimeRequestFixture#registerFailRequestFixture")
    void 시간_등록_요청_시_형식_검증에_실패하면_예외가_발생한다(ReservationTimeRequest request, String exceptionMessage) {
        RestAssuredMockMvc.given().spec(adminSpec()).log().all()
                .body(request)
                .when().post("/api/admin/times")
                .then().log().all()
                .status(HttpStatus.BAD_REQUEST)
                .body(containsString(exceptionMessage));
    }

    @Test
    void 시간_등록에_성공하면_201_Created_상태와_정상_응답이_반환된다() {
        // given
        ReservationTimeRequest request = ReservationTimeRequestFixture.registerSuccessRequestFixture();

        ReservationTimeResponse expected = new ReservationTimeResponse(1L, request.startAt());
        when(reservationTimeService.register(any(ReservationTimeRequest.class))).thenReturn(expected);

        // when & then
        ReservationTimeResponse response = RestAssuredMockMvc.given().spec(adminSpec()).log().all()
                .body(request)
                .when().post("/api/admin/times")
                .then().log().all()
                .status(HttpStatus.CREATED)
                .header("Location", containsString("/api/admin/times/1"))
                .extract().as(new TypeRef<>() {
                });

        assertThat(response).isEqualTo(expected);
    }

    @ParameterizedTest
    @ValueSource(ints = {0, -1})
    void 시간_삭제를_요청하는_식별자가_양수가_아니라면_예외가_발생한다(int timeId) {
        // when & then
        RestAssuredMockMvc.given().spec(adminSpec()).log().all()
                .when().delete("/api/admin/times/" + timeId)
                .then().log().all()
                .status(HttpStatus.BAD_REQUEST)
                .body(containsString("예약 시간 제거 식별자는 양수여야 합니다."));
    }

    @Test
    void 정상적인_ID로_시간_삭제_요청시_204_응답을_한다() {
        // when & then
        RestAssuredMockMvc.given().spec(adminSpec()).log().all()
                .when().delete("/api/admin/times/1")
                .then().log().all()
                .status(HttpStatus.NO_CONTENT);
        verify(reservationTimeService, times(1)).remove(anyLong());
    }

    @Test
    void 전체_시간_조회_요청시_200OK와_시간_정보들을_응답한다() {
        // given
        ReservationTimeResponses expected = new ReservationTimeResponses(
                List.of(
                        new ReservationTimeResponse(1L, LocalTime.of(10, 0)),
                        new ReservationTimeResponse(2L, LocalTime.of(11, 0))
                )
        );
        when(reservationTimeService.getAllReservationTimes()).thenReturn(expected.responses());

        // when & then
        ReservationTimeResponses response = RestAssuredMockMvc.given().spec(adminSpec()).log().all()
                .when().get("/api/admin/times")
                .then().log().all()
                .status(HttpStatus.OK)
                .extract().as(new TypeRef<>() {
                });

        assertThat(response).isEqualTo(expected);
    }
}
