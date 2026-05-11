package roomescape.controller.admin;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.BDDMockito.willThrow;

import io.restassured.common.mapper.TypeRef;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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
import roomescape.dto.response.ReservationResponseDto;
import roomescape.fixture.ReservationRequestDtoFixture;
import roomescape.service.ReservationService;

@WebMvcTest(AdminReservationController.class)
class AdminReservationControllerTest {

    private final Time time = new Time(1L, LocalTime.of(13, 0));
    private final Theme theme = new Theme(1L, new Name("방탈출테마"), "http://example.com/img.jpg", "방탈출 테마 설명");
    private final Reservation reservation = new Reservation(1L, "유저1", LocalDate.now().plusDays(1), time, theme);

    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private ReservationService reservationService;

    @BeforeEach
    void setUp() {
        RestAssuredMockMvc.mockMvc(mockMvc);
    }

    @Nested
    class Get {

        @Test
        @DisplayName("전체 예약 목록을 조회한다")
        void returnsAllReservations() {
            List<Reservation> reservations = List.of(reservation);
            given(reservationService.findAll()).willReturn(reservations);
            List<ReservationResponseDto> expected = reservations.stream()
                    .map(ReservationResponseDto::from)
                    .toList();

            List<ReservationResponseDto> actual = RestAssuredMockMvc.given()
                    .when().get("/admin/reservations")
                    .then()
                    .status(HttpStatus.OK)
                    .extract().as(new TypeRef<>() {});

            assertThat(actual).isEqualTo(expected);
            then(reservationService).should().findAll();
        }

        @Test
        @DisplayName("존재하는 예약 id를 조회하면 200을 반환한다")
        void returnsReservationById() {
            given(reservationService.findById(reservation.getId())).willReturn(reservation);
            ReservationResponseDto expected = ReservationResponseDto.from(reservation);

            ReservationResponseDto actual = RestAssuredMockMvc.given()
                    .when().get("/admin/reservations/" + reservation.getId())
                    .then()
                    .status(HttpStatus.OK)
                    .extract().as(ReservationResponseDto.class);

            assertThat(actual).isEqualTo(expected);
        }

        @Test
        @DisplayName("존재하지 않는 예약 id를 조회하면 404를 반환한다")
        void returnsNotFoundWhenIdNotExists() {
            long notExistsId = -1;
            given(reservationService.findById(notExistsId)).willThrow(new NotFoundException("존재하지 않는 예약입니다."));

            RestAssuredMockMvc.given()
                    .when().get("/admin/reservations/" + notExistsId)
                    .then()
                    .status(HttpStatus.NOT_FOUND);
        }
    }

    @Nested
    class Post {

        static Stream<Arguments> invalidReservationRequests() {
            return Stream.of(
                    Arguments.of("name이 공백", ReservationRequestDtoFixture.withBlankName()),
                    Arguments.of("name이 20자 초과", ReservationRequestDtoFixture.withNameExceedingMaxLength()),
                    Arguments.of("date가 null", ReservationRequestDtoFixture.withNullDate()),
                    Arguments.of("timeId가 null", ReservationRequestDtoFixture.withNullTimeId()),
                    Arguments.of("themeId가 null", ReservationRequestDtoFixture.withNullThemeId())
            );
        }

        @Test
        @DisplayName("유효한 요청으로 예약을 생성하면 201을 반환한다")
        void createsReservation() {
            ReservationRequestDto requestDto = new ReservationRequestDto(reservation.getName(), reservation.getDate(),
                    time.getId(), theme.getId());
            given(reservationService.create(any(ReservationRequestDto.class))).willReturn(reservation);
            ReservationResponseDto expected = ReservationResponseDto.from(reservation);

            ReservationResponseDto actual = RestAssuredMockMvc.given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(requestDto)
                    .when().post("/admin/reservations")
                    .then()
                    .status(HttpStatus.CREATED)
                    .header("Location", "http://localhost/admin/reservations/" + reservation.getId())
                    .extract().as(ReservationResponseDto.class);

            assertThat(actual).isEqualTo(expected);
        }

        @ParameterizedTest(name = "{0}")
        @MethodSource("invalidReservationRequests")
        @DisplayName("유효하지 않은 요청으로 예약을 생성하면 400을 반환한다")
        void returnsValidationError(String description, ReservationRequestDto invalidRequest) {
            RestAssuredMockMvc.given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(invalidRequest)
                    .when().post("/admin/reservations")
                    .then()
                    .status(HttpStatus.BAD_REQUEST);
        }
    }

    @Nested
    class Delete {

        @Test
        @DisplayName("예약을 삭제하면 204를 반환한다")
        void deletesReservation() {
            willDoNothing().given(reservationService).delete(reservation.getId());

            RestAssuredMockMvc.given()
                    .when().delete("/admin/reservations/" + reservation.getId())
                    .then()
                    .status(HttpStatus.NO_CONTENT);
        }

        @Test
        @DisplayName("존재하지 않는 예약을 삭제하면 404를 반환한다")
        void returnsNotFoundWhenIdNotExists() {
            Long notExistsId = 1L;
            willThrow(new NotFoundException("존재하지 않는 예약입니다.")).given(reservationService).delete(notExistsId);

            RestAssuredMockMvc.given()
                    .when().delete("/admin/reservations/" + notExistsId.intValue())
                    .then()
                    .status(HttpStatus.NOT_FOUND);
        }
    }
}
