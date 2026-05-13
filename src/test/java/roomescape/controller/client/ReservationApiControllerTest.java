package roomescape.controller.client;

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
import roomescape.common.Page;
import roomescape.common.Pageable;
import roomescape.controller.BaseControllerUnitTest;
import roomescape.controller.client.api.ReservationApiController;
import roomescape.controller.client.api.dto.ReservationRequest;
import roomescape.controller.client.api.dto.ReservationResponse;
import roomescape.controller.client.fixture.ReservationApiRequestFixture;
import roomescape.query.ReservationQuery;
import roomescape.query.ReservationSearchCondition;
import roomescape.query.ReservationSearchResponse;
import roomescape.service.ReservationService;
import roomescape.service.command.ReservationCommand;
import roomescape.service.result.ReservationResult;
import roomescape.service.result.ReservationTimeResult;

@WebMvcTest(ReservationApiController.class)
class ReservationApiControllerTest extends BaseControllerUnitTest {

    @MockitoBean
    private ReservationService reservationService;
    @MockitoBean
    private ReservationQuery reservationQuery;

    @BeforeEach
    void setUp(WebApplicationContext webApplicationContext) {
        mockMvcSetting(webApplicationContext);
    }

    @ParameterizedTest(name = "요청 정보가 {0} 일 때, 예외 메세지 \"{1}\"가 발생한다.")
    @MethodSource("roomescape.controller.client.fixture.ReservationApiRequestFixture#reserveFailRequestFixture")
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
        ReservationRequest body = ReservationApiRequestFixture.reserveSuccessRequestFixture();
        ReservationTimeResult timeResult = new ReservationTimeResult(1L, LocalTime.now(), "ACTIVE");
        ReservationResult result = new ReservationResult(1L, "이프", LocalDate.now(), timeResult);
        when(reservationService.reserve(any(ReservationCommand.class))).thenReturn(result);

        // when & then
        ReservationResponse response = RestAssuredMockMvc.given().spec(defaultSpec()).log().all()
                .body(body)
                .when().post("/api/reservations")
                .then().log().all()
                .status(HttpStatus.CREATED)
                .extract().as(new TypeRef<>() {
                });

        assertThat(response).isEqualTo(ReservationResponse.from(result));
    }

    @Test
    void 사용자_이름으로_예약_정보를_조회할_수_있다() {
        // given
        ReservationSearchResponse searchResponse =
                new ReservationSearchResponse(1L, "이름", LocalDate.now(), LocalTime.now(), "테마명");
        Page<ReservationSearchResponse> result = Page.of(10, 10, List.of(searchResponse));
        when(reservationQuery.search(any(ReservationSearchCondition.class), any(Pageable.class))).thenReturn(result);

        // when
        Page<ReservationSearchResponse> response = RestAssuredMockMvc.given().spec(defaultSpec()).log().all()
                .params("name", "이프")
                .when().get("/api/reservations")
                .then().log().all()
                .status(HttpStatus.OK)
                .extract().as(new TypeRef<>() {
                });

        // then
        assertThat(response).isEqualTo(result);
    }

    @Test
    void 사용자_이름_정보없이_예약_목록을_조회한다면_예외가_발생한다() {
        // when
        RestAssuredMockMvc.given().spec(defaultSpec()).log().all()
                .when().get("/api/reservations")
                .then().log().all()
                .status(HttpStatus.BAD_REQUEST)
                .body(containsString("[name] 예약 정보 검색 시 사용자 명이 필요합니다."));
    }
}
