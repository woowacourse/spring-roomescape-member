package roomescape.controller.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import io.restassured.module.mockmvc.RestAssuredMockMvc;
import java.time.LocalDate;
import java.time.LocalTime;
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
import roomescape.common.exception.ConflictException;
import roomescape.common.exception.NotFoundException;
import roomescape.domain.Reservation;
import roomescape.domain.Theme;
import roomescape.domain.Time;
import roomescape.domain.vo.Name;
import roomescape.dto.request.ReservationRequestDto;
import roomescape.dto.response.ReservationResponseDto;
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
        @DisplayName("유효한 요청으로 예약을 생성하면 200을 반환한다")
        void createsReservation() {
            ReservationRequestDto requestDto = new ReservationRequestDto("유저1", LocalDate.of(2026, 5, 10), 1L, 1L);
            given(reservationService.create(any())).willReturn(reservation);
            ReservationResponseDto expected = ReservationResponseDto.from(reservation);

            ReservationResponseDto actual = RestAssuredMockMvc.given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(requestDto)
                    .when().post("/reservations")
                    .then()
                    .status(HttpStatus.OK)
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
                    .when().post("/reservations")
                    .then()
                    .status(HttpStatus.BAD_REQUEST);
        }

        @Test
        @DisplayName("존재하지 않는 시간으로 예약을 생성하면 404를 반환한다")
        void returnsNotFoundWhenTimeNotExists() {
            ReservationRequestDto requestDto = new ReservationRequestDto("유저1", LocalDate.of(2026, 5, 10), 999L, 1L);
            given(reservationService.create(any())).willThrow(new NotFoundException("존재하지 않는 시간입니다."));

            RestAssuredMockMvc.given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(requestDto)
                    .when().post("/reservations")
                    .then()
                    .status(HttpStatus.NOT_FOUND);
        }

        @Test
        @DisplayName("중복된 예약을 생성하면 409를 반환한다")
        void returnsConflictWhenDuplicateReservation() {
            ReservationRequestDto requestDto = new ReservationRequestDto("유저1", LocalDate.of(2026, 5, 10), 1L, 1L);
            given(reservationService.create(any())).willThrow(new ConflictException("이미 존재하는 예약이 있습니다."));

            RestAssuredMockMvc.given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(requestDto)
                    .when().post("/reservations")
                    .then()
                    .status(HttpStatus.CONFLICT);
        }
    }
}
