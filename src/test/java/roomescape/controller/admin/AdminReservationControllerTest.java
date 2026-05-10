package roomescape.controller.admin;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.BDDMockito.willThrow;

import io.restassured.module.mockmvc.RestAssuredMockMvc;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
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
import roomescape.common.exception.NotFoundException;
import roomescape.domain.Reservation;
import roomescape.domain.Theme;
import roomescape.domain.Time;
import roomescape.domain.vo.Name;
import roomescape.dto.request.ReservationRequestDto;
import roomescape.fixture.ReservationRequestDtoFixture;
import roomescape.service.ReservationService;

@WebMvcTest(AdminReservationController.class)
class AdminReservationControllerTest {

    private final Time time = new Time(1L, LocalTime.of(13, 0));
    private final Theme theme = new Theme(1L, new Name("방탈출테마"), "http://example.com/img.jpg", "방탈출 테마 설명");
    private final Reservation reservation = new Reservation(1L, "유저1", LocalDate.now().plusDays(1), time, theme);
    private final ReservationRequestDto requestDto =
            new ReservationRequestDto(reservation.getName(), reservation.getDate(), time.getId(), theme.getId());

    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private ReservationService reservationService;

    @BeforeEach
    void setUp() {
        RestAssuredMockMvc.mockMvc(mockMvc);
    }

    @Nested
    class GET {

        @Test
        void 전체_예약_목록을_조회한다() {
            List<Reservation> reservations = List.of(reservation);
            given(reservationService.findAll())
                    .willReturn(reservations);

            RestAssuredMockMvc.given()
                    .when().get("/admin/reservations")
                    .then()
                    .status(HttpStatus.OK)
                    .body("$", hasSize(reservations.size()));

            then(reservationService).should().findAll();
        }

        @Test
        void 존재하는_예약_id를_조회하면_200을_반환한다() {
            given(reservationService.findById(reservation.getId())).willReturn(reservation);

            RestAssuredMockMvc.given().log().all()
                    .when().get("/admin/reservations/" + reservation.getId())
                    .then().log().all()
                    .status(HttpStatus.OK)
                    .body("id", equalTo(reservation.getId().intValue()));
        }

        @Test
        void 존재하지_않는_예약_id를_조회하면_404를_반환한다() {
            long notExistsId = -1;
            given(reservationService.findById(notExistsId)).willThrow(new NotFoundException("존재하지 않는 예약입니다."));

            RestAssuredMockMvc.given().log().all()
                    .when().get("/admin/reservations/" + notExistsId)
                    .then().log().all()
                    .status(HttpStatus.NOT_FOUND);
        }
    }

    @Nested
    class POST {

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
        void 유효한_요청으로_예약을_생성하면_201을_반환한다() {
            given(reservationService.create(any(ReservationRequestDto.class))).willReturn(reservation);

            RestAssuredMockMvc.given().log().all()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(requestDto)
                    .when().post("/admin/reservations")
                    .then().log().all()
                    .status(HttpStatus.CREATED)
                    .header("Location", ("http://localhost/admin/reservations/" + reservation.getId()))
                    .body("id", equalTo(reservation.getId().intValue()));
        }

        @ParameterizedTest(name = "{0}")
        @MethodSource("유효하지_않은_예약_요청_목록")
        void 유효하지_않은_요청으로_예약을_생성하면_400을_반환한다(String description, ReservationRequestDto invalidRequest) {
            RestAssuredMockMvc.given().log().all()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(invalidRequest)
                    .when().post("/admin/reservations")
                    .then().log().all()
                    .status(HttpStatus.BAD_REQUEST);
        }
    }

    @Nested
    class DELETE {

        @Test
        void 예약을_삭제하면_204를_반환한다() {
            willDoNothing().given(reservationService).delete(reservation.getId());

            RestAssuredMockMvc.given().log().all()
                    .when().delete("/admin/reservations/" + reservation.getId())
                    .then().log().all()
                    .status(HttpStatus.NO_CONTENT);
        }

        @Test
        void 존재하지_않는_예약을_삭제하면_404를_반환한다() {
            Long notExistsId = 1L;
            willThrow(new NotFoundException("존재하지 않는 예약입니다.")).given(reservationService).delete(notExistsId);

            RestAssuredMockMvc.given().log().all()
                    .when().delete("/admin/reservations/" + notExistsId.intValue())
                    .then().log().all()
                    .status(HttpStatus.NOT_FOUND);
        }
    }
}
