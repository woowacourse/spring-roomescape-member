package roomescape.controller.admin;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.when;

import io.restassured.common.mapper.TypeRef;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.web.context.WebApplicationContext;
import roomescape.controller.BaseControllerUnitTest;
import roomescape.domain.ReservationStatus;
import roomescape.service.ReservationService;
import roomescape.web.controller.admin.AdminReservationController;
import roomescape.web.dto.reservation.ReservationResponse;
import roomescape.web.dto.reservation.ReservationResponses;
import roomescape.web.dto.reservationTime.ReservationTimeResponse;
import roomescape.web.dto.theme.ThemeResponse;

@WebMvcTest(AdminReservationController.class)
class AdminReservationControllerTest extends BaseControllerUnitTest {

    @MockitoBean
    private ReservationService reservationService;

    @BeforeEach
    void setUp(WebApplicationContext webApplicationContext) {
        mockMvcSetting(webApplicationContext);
    }

    @Test
    void 전체_예약_정보_조회_요청에_성공하면_200_OK와_예약_정보들을_응답한다() {
        // given
        ReservationTimeResponse timeResponse = new ReservationTimeResponse(1L, LocalTime.of(10, 0));
        ThemeResponse themeResponse = new ThemeResponse(1L, "바니의 집", "바니의 테마입니다.", "http://image.png.image.com");

        ReservationResponses expected = new ReservationResponses(
                List.of(
                        new ReservationResponse(1L, "웨지", LocalDate.of(2028, 5, 9),
                                timeResponse, themeResponse, ReservationStatus.RESERVED),
                        new ReservationResponse(2L, "바니", LocalDate.of(2028, 5, 10),
                                timeResponse, themeResponse, ReservationStatus.RESERVED)
                ));
        when(reservationService.getAllReservationsByPaging(0, 10)).thenReturn(expected.responses());

        // when & then
        ReservationResponses response = RestAssuredMockMvc.given().spec(adminSpec()).log().all()
                .queryParam("page", "0")
                .queryParam("size", "10")
                .when().get("/api/admin/reservations")
                .then().log().all()
                .status(HttpStatus.OK)
                .extract().as(new TypeRef<>() {
                });

        assertThat(response).isEqualTo(expected);
    }

    @Test
    void 전체_예약_정보_조회_요청_시_페이지가_없으면_400_BAD_REQUEST를_응답한다() {
        // when & then
        RestAssuredMockMvc.given().spec(adminSpec()).log().all()
                .queryParam("size", "10")
                .when().get("/api/admin/reservations")
                .then().log().all()
                .status(HttpStatus.BAD_REQUEST)
                .body(containsString("page 파라미터가 누락 되었습니다."));
    }

    @Test
    void 전체_예약_정보_조회_요청_시_조회_개수가_없으면_400_BAD_REQUEST를_응답한다() {
        // when & then
        RestAssuredMockMvc.given().spec(adminSpec()).log().all()
                .queryParam("page", "0")
                .when().get("/api/admin/reservations")
                .then().log().all()
                .status(HttpStatus.BAD_REQUEST)
                .body(containsString("size 파라미터가 누락 되었습니다."));
    }
}
