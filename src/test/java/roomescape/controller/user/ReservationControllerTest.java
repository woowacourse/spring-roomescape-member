package roomescape.controller.user;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import io.restassured.module.mockmvc.RestAssuredMockMvc;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import roomescape.common.exception.ConflictException;
import roomescape.common.exception.NotFoundException;
import roomescape.domain.Reservation;
import roomescape.domain.Theme;
import roomescape.domain.Time;
import roomescape.domain.vo.Name;
import roomescape.dto.request.ReservationRequestDto;
import roomescape.fixture.ReservationRequestDtoFixture;
import roomescape.service.ReservationService;

@WebMvcTest(ReservationController.class)
class ReservationControllerTest {

    private final Time time = new Time(1L, LocalTime.of(13, 0));
    private final Theme theme = new Theme(1L, new Name("방탈출테마"), "http://example.com/img.jpg", "방탈출 테마 설명");
    private final Reservation reservation = new Reservation(1L, "유저1", LocalDate.of(2026, 5, 10), time, theme);

    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private ReservationService reservationService;

    @BeforeEach
    void setUp() {
        RestAssuredMockMvc.mockMvc(mockMvc);
    }

    @Test
    void 유효한_요청으로_예약을_생성하면_200을_반환한다() {
        ReservationRequestDto requestDto = new ReservationRequestDto("유저1", LocalDate.of(2026, 5, 10), 1L, 1L);
        given(reservationService.create(any())).willReturn(reservation);

        RestAssuredMockMvc.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(requestDto)
                .when().post("/reservations")
                .then().log().all()
                .status(HttpStatus.OK)
                .body("id", equalTo(1))
                .body("name", equalTo("유저1"));
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("유효하지_않은_예약_요청_목록")
    void 유효하지_않은_요청으로_예약을_생성하면_400을_반환한다(String description, ReservationRequestDto invalidRequest) {
        RestAssuredMockMvc.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(invalidRequest)
                .when().post("/reservations")
                .then().log().all()
                .status(HttpStatus.BAD_REQUEST);
    }

    static Stream<Arguments> 유효하지_않은_예약_요청_목록() {
        return Stream.of(
                Arguments.of("name이 공백", ReservationRequestDtoFixture.withBlankName()),
                Arguments.of("name이 20자 초과", ReservationRequestDtoFixture.withNameExceedingMaxLength()),
                Arguments.of("date가 null", ReservationRequestDtoFixture.withNullDate()),
                Arguments.of("timeId가 null", ReservationRequestDtoFixture.withNullTimeId()),
                Arguments.of("themeId가 null", ReservationRequestDtoFixture.withNullThemeId())
        );
    }

    @Test
    void 존재하지_않는_시간으로_예약을_생성하면_404를_반환한다() {
        ReservationRequestDto requestDto = new ReservationRequestDto("유저1", LocalDate.of(2026, 5, 10), 999L, 1L);
        given(reservationService.create(any())).willThrow(new NotFoundException("존재하지 않는 시간입니다."));

        RestAssuredMockMvc.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(requestDto)
                .when().post("/reservations")
                .then().log().all()
                .status(HttpStatus.NOT_FOUND);
    }

    @Test
    void 중복된_예약을_생성하면_409를_반환한다() {
        ReservationRequestDto requestDto = new ReservationRequestDto("유저1", LocalDate.of(2026, 5, 10), 1L, 1L);
        given(reservationService.create(any())).willThrow(new ConflictException("이미 존재하는 예약이 있습니다."));

        RestAssuredMockMvc.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(requestDto)
                .when().post("/reservations")
                .then().log().all()
                .status(HttpStatus.CONFLICT);
    }
}
