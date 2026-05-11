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
import org.junit.jupiter.api.DisplayName;
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
    class Get {

        @Test
        @DisplayName("전체 시간 목록을 조회하면 200을 반환한다")
        void returnsAllTimes() {
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
        @DisplayName("존재하는 시간 id를 조회하면 200을 반환한다")
        void returnsTimeById() {
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
        @DisplayName("존재하지 않는 시간 id를 조회하면 404를 반환한다")
        void returnsNotFoundWhenIdNotExists() {
            given(timeService.findById(999L)).willThrow(new NotFoundException("존재하지 않는 시간입니다."));

            RestAssuredMockMvc.given()
                    .when().get("/admin/times/999")
                    .then()
                    .status(HttpStatus.NOT_FOUND);
        }
    }

    @Nested
    class Post {

        @Test
        @DisplayName("유효한 요청으로 시간을 생성하면 201을 반환한다")
        void createsTime() {
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
        @DisplayName("startAt이 null이면 400을 반환한다")
        void returnsValidationErrorWhenStartAtIsNull() {
            TimeRequestDto requestDto = new TimeRequestDto(null);

            RestAssuredMockMvc.given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(requestDto)
                    .when().post("/admin/times")
                    .then()
                    .status(HttpStatus.BAD_REQUEST);
        }

        @Test
        @DisplayName("중복된 시간을 생성하면 409를 반환한다")
        void returnsConflictWhenDuplicateTime() {
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
    class Delete {

        @Test
        @DisplayName("시간을 삭제하면 204를 반환한다")
        void deletesTime() {
            willDoNothing().given(timeService).delete(time.getId());

            RestAssuredMockMvc.given()
                    .when().delete("/admin/times/" + time.getId())
                    .then()
                    .status(HttpStatus.NO_CONTENT);
        }

        @Test
        @DisplayName("예약이 있는 시간을 삭제하면 409를 반환한다")
        void returnsConflictWhenTimeHasReservation() {
            willThrow(new ConflictException("해당 시간에 예약이 존재합니다.")).given(timeService).delete(time.getId());

            RestAssuredMockMvc.given()
                    .when().delete("/admin/times/" + time.getId())
                    .then()
                    .status(HttpStatus.CONFLICT);
        }
    }
}
