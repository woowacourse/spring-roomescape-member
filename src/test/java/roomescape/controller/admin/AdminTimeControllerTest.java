package roomescape.controller.admin;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.BDDMockito.willThrow;

import io.restassured.common.mapper.TypeRef;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import roomescape.common.exception.ConflictException;
import roomescape.common.exception.NotFoundException;
import roomescape.domain.Time;
import roomescape.dto.request.TimeRequestDto;
import roomescape.dto.response.TimeResponseDto;
import roomescape.service.TimeService;

@WebMvcTest(AdminTimeController.class)
class AdminTimeControllerTest {

    private final Time time = new Time(1L, LocalTime.of(13, 0));

    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private TimeService timeService;

    @BeforeEach
    void setUp() {
        RestAssuredMockMvc.mockMvc(mockMvc);
    }

    @Nested
    class GET {

        @Test
        void 전체_시간_목록을_조회하면_200을_반환한다() {
            given(timeService.findAll()).willReturn(List.of(time));
            List<TimeResponseDto> expected = List.of(TimeResponseDto.from(time));

            List<TimeResponseDto> actual = RestAssuredMockMvc.given()
                    .when().get("/admin/times")
                    .then()
                    .status(HttpStatus.OK)
                    .extract().as(new TypeRef<>() {});

            assertThat(actual).isEqualTo(expected);
        }

        @Test
        void 존재하는_시간_id를_조회하면_200을_반환한다() {
            given(timeService.findById(time.getId())).willReturn(time);
            TimeResponseDto expected = TimeResponseDto.from(time);

            TimeResponseDto actual = RestAssuredMockMvc.given()
                    .when().get("/admin/times/" + time.getId())
                    .then()
                    .status(HttpStatus.OK)
                    .extract().as(TimeResponseDto.class);

            assertThat(actual).isEqualTo(expected);
        }

        @Test
        void 존재하지_않는_시간_id를_조회하면_404를_반환한다() {
            given(timeService.findById(999L)).willThrow(new NotFoundException("존재하지 않는 시간입니다."));

            RestAssuredMockMvc.given()
                    .when().get("/admin/times/999")
                    .then()
                    .status(HttpStatus.NOT_FOUND);
        }
    }

    @Nested
    class POST {

        @Test
        void 유효한_요청으로_시간을_생성하면_201을_반환한다() {
            TimeRequestDto requestDto = new TimeRequestDto(LocalTime.of(13, 0));
            given(timeService.create(any())).willReturn(time);
            TimeResponseDto expected = TimeResponseDto.from(time);

            TimeResponseDto actual = RestAssuredMockMvc.given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(requestDto)
                    .when().post("/admin/times")
                    .then()
                    .status(HttpStatus.CREATED)
                    .header("Location", "http://localhost/admin/times/" + time.getId())
                    .extract().as(TimeResponseDto.class);

            assertThat(actual).isEqualTo(expected);
        }

        @Test
        void startAt이_null이면_400을_반환한다() {
            TimeRequestDto requestDto = new TimeRequestDto(null);

            RestAssuredMockMvc.given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(requestDto)
                    .when().post("/admin/times")
                    .then()
                    .status(HttpStatus.BAD_REQUEST);
        }

        @Test
        void 중복된_시간을_생성하면_409를_반환한다() {
            TimeRequestDto requestDto = new TimeRequestDto(LocalTime.of(13, 0));
            given(timeService.create(any())).willThrow(new ConflictException("이미 존재하는 시간입니다."));

            RestAssuredMockMvc.given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(requestDto)
                    .when().post("/admin/times")
                    .then()
                    .status(HttpStatus.CONFLICT);
        }
    }

    @Nested
    class DELETE {

        @Test
        void 시간을_삭제하면_204를_반환한다() {
            willDoNothing().given(timeService).delete(time.getId());

            RestAssuredMockMvc.given()
                    .when().delete("/admin/times/" + time.getId())
                    .then()
                    .status(HttpStatus.NO_CONTENT);
        }

        @Test
        void 예약이_있는_시간을_삭제하면_409를_반환한다() {
            willThrow(new ConflictException("해당 시간에 예약이 존재합니다.")).given(timeService).delete(time.getId());

            RestAssuredMockMvc.given()
                    .when().delete("/admin/times/" + time.getId())
                    .then()
                    .status(HttpStatus.CONFLICT);
        }
    }
}
