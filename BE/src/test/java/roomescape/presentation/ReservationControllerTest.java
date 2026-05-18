package roomescape.presentation;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import roomescape.application.ReservationService;
import roomescape.entity.Reservation;
import roomescape.entity.ReservationTime;
import roomescape.entity.Theme;
import roomescape.global.auth.AdminInterceptor;
import roomescape.global.exception.ErrorCode;
import roomescape.global.exception.customException.ConflictException;
import roomescape.global.exception.customException.DomainRuleViolationException;
import roomescape.global.exception.customException.NotFoundException;
import roomescape.presentation.dto.ReservationRequest;
import roomescape.presentation.dto.ReservationUpdateRequest;

@WebMvcTest(ReservationController.class)
@Import({AdminInterceptor.class, ReservationControllerTest.TestWebConfig.class})
class ReservationControllerTest {

    private static final LocalDate TEST_DATE = LocalDate.now();
    private static final String TEST_DATE_STR = TEST_DATE.toString();
    private static final String TEST_NAME = "홍길동";
    private static final Long TEST_TIME_ID = 1L;
    private static final String TEST_START_AT = "10:00";
    private static final Long TEST_THEME_ID = 1L;
    private static final String TEST_THEME_NAME = "테스트-테마";

    @TestConfiguration
    static class TestWebConfig implements WebMvcConfigurer {
        @Autowired
        private AdminInterceptor adminInterceptor;

        @Override
        public void addInterceptors(InterceptorRegistry registry) {
            registry.addInterceptor(adminInterceptor).addPathPatterns("/**");
        }
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ReservationService reservationService;

    @Test
    @DisplayName("POST /reservations - 정상 저장 시 201과 응답 본문을 반환한다.")
    void createReservation_success() throws Exception {
        // given
        Reservation reservation = sampleReservation(1L, TEST_NAME, TEST_DATE, TEST_TIME_ID, TEST_START_AT,
                TEST_THEME_ID, TEST_THEME_NAME);
        given(reservationService.save(TEST_NAME, TEST_DATE, TEST_TIME_ID, TEST_THEME_ID)).willReturn(reservation);

        ReservationRequest request = new ReservationRequest(TEST_NAME, TEST_DATE, TEST_TIME_ID, TEST_THEME_ID);

        // when & then
        ResultActions actions = mockMvc.perform(post("/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "http://localhost/reservations/1"));

        verifyReservationResponse(actions, reservation);
        then(reservationService).should().save(TEST_NAME, TEST_DATE, TEST_TIME_ID, TEST_THEME_ID);
    }

    @Test
    @DisplayName("POST /reservations - 예약자 성함이 누락된 경우 400 에러를 반환한다.")
    void createReservation_fail_missing_name() throws Exception {
        Map<String, Object> body = new HashMap<>();
        body.put("date", TEST_DATE_STR);
        body.put("timeId", TEST_TIME_ID);
        body.put("themeId", TEST_THEME_ID);

        mockMvc.perform(post("/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorType").value("BUSINESS"))
                .andExpect(jsonPath("$.message").value("예약자 성함이 누락되었습니다. 이름을 입력해주세요."));
    }

    @Test
    @DisplayName("POST /reservations - 예약 날짜가 누락된 경우 400 에러를 반환한다.")
    void createReservation_fail_missing_date() throws Exception {
        Map<String, Object> body = new HashMap<>();
        body.put("name", TEST_NAME);
        body.put("timeId", TEST_TIME_ID);
        body.put("themeId", TEST_THEME_ID);

        mockMvc.perform(post("/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorType").value("BUSINESS"))
                .andExpect(jsonPath("$.message").value("예약 날짜가 선택되지 않았습니다. 날짜를 선택해주세요."));
    }

    @Test
    @DisplayName("POST /reservations - 예약 시간이 누락된 경우 400 에러와 명확한 메시지를 반환한다.")
    void createReservation_fail_when_timeId_is_null() throws Exception {
        // given
        Map<String, Object> body = new HashMap<>();
        body.put("name", TEST_NAME);
        body.put("date", TEST_DATE_STR);
        body.put("themeId", TEST_THEME_ID);

        // when & then
        mockMvc.perform(post("/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorType").value("BUSINESS"))
                .andExpect(jsonPath("$.message").value("예약 시간이 선택되지 않았습니다. 시간을 선택해주세요."));
    }

    @Test
    @DisplayName("POST /reservations - 날짜 형식이 yyyy-MM-dd가 아닌 경우 INVALID_HTTP_MESSAGE 에러를 반환한다.")
    void createReservation_fail_invalid_date_format() throws Exception {
        Map<String, Object> body = new HashMap<>();
        body.put("name", TEST_NAME);
        body.put("date", "2026/08/16");
        body.put("timeId", TEST_TIME_ID);
        body.put("themeId", TEST_THEME_ID);

        mockMvc.perform(post("/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorType").value("BUSINESS"))
                .andExpect(jsonPath("$.message").value("요청 정보가 올바르지 않습니다. 입력 내용을 다시 확인해주세요."));
    }

    @Test
    @DisplayName("POST /reservations - themeId가 숫자가 아닌 문자열로 오면 INVALID_HTTP_MESSAGE 에러를 반환한다.")
    void createReservation_fail_invalid_themeId_type() throws Exception {
        Map<String, Object> body = new HashMap<>();
        body.put("name", TEST_NAME);
        body.put("date", TEST_DATE_STR);
        body.put("timeId", TEST_TIME_ID);
        body.put("themeId", "공포 테마");

        mockMvc.perform(post("/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorType").value("BUSINESS"))
                .andExpect(jsonPath("$.message").value("요청 정보가 올바르지 않습니다. 입력 내용을 다시 확인해주세요."));
    }

    @Test
    @DisplayName("POST /reservations - 이미 존재하는 예약인 경우 409 에러를 반환한다.")
    void createReservation_fail_duplicated() throws Exception {
        given(reservationService.save(anyString(), any(LocalDate.class), anyLong(), anyLong()))
                .willThrow(new ConflictException(ErrorCode.RESERVATION_DUPLICATED));

        ReservationRequest request = new ReservationRequest(TEST_NAME, TEST_DATE, TEST_TIME_ID, TEST_THEME_ID);

        mockMvc.perform(post("/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.errorType").value("BUSINESS"))
                .andExpect(jsonPath("$.message").value("동일한 날짜, 시간, 테마의 예약이 이미 존재합니다. 다른 조건을 선택해주세요."));
    }

    @Test
    @DisplayName("POST /reservations - 과거의 날짜나 시간으로 예약 시 422 에러를 반환한다.")
    void createReservation_fail_past_date() throws Exception {
        given(reservationService.save(anyString(), any(LocalDate.class), anyLong(), anyLong()))
                .willThrow(new DomainRuleViolationException(ErrorCode.ILLEGAL_PAST_DATE));

        ReservationRequest request = new ReservationRequest(TEST_NAME, TEST_DATE.minusDays(1), TEST_TIME_ID,
                TEST_THEME_ID);

        mockMvc.perform(post("/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.errorType").value("BUSINESS"))
                .andExpect(jsonPath("$.message").value("지나간 날짜나 시간으로는 예약을 생성하거나 삭제할 수 없습니다. 현재 이후의 시간을 선택해주세요."));
    }

    @Test
    @DisplayName("POST /reservations - 존재하지 않는 시간(timeId)으로 예약 시 404 에러를 반환한다.")
    void createReservation_fail_time_not_found() throws Exception {
        given(reservationService.save(anyString(), any(LocalDate.class), anyLong(), anyLong()))
                .willThrow(new NotFoundException(ErrorCode.RESERVATION_TIME_NOT_FOUND));

        ReservationRequest request = new ReservationRequest(TEST_NAME, TEST_DATE, 999L, TEST_THEME_ID);

        mockMvc.perform(post("/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorType").value("BUSINESS"))
                .andExpect(jsonPath("$.message").value("선택하신 예약 시간 정보가 존재하지 않습니다."));
    }

    @Test
    @DisplayName("POST /reservations - 존재하지 않는 테마(themeId)로 예약 시 404 에러를 반환한다.")
    void createReservation_fail_theme_not_found() throws Exception {
        given(reservationService.save(anyString(), any(LocalDate.class), anyLong(), anyLong()))
                .willThrow(new NotFoundException(ErrorCode.THEME_NOT_FOUND));

        ReservationRequest request = new ReservationRequest(TEST_NAME, TEST_DATE, TEST_TIME_ID, 999L);

        mockMvc.perform(post("/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorType").value("BUSINESS"))
                .andExpect(jsonPath("$.message").value("선택하신 테마 정보가 존재하지 않습니다."));
    }

    @Test
    @DisplayName("POST /reservations - 잘못된 Content-Type(text/plain)인 경우 415 에러를 반환한다.")
    void createReservation_fail_unsupported_media_type() throws Exception {
        mockMvc.perform(post("/reservations")
                        .contentType(MediaType.TEXT_PLAIN)
                        .content("name=brown"))
                .andExpect(status().isUnsupportedMediaType())
                .andExpect(jsonPath("$.message").value("요청 정보가 올바르지 않습니다. 입력 내용을 다시 확인해주세요."));
    }

    @Test
    @DisplayName("GET /reservations - 필터 없이 호출하면 전체 목록을 반환한다.")
    void readReservations_no_filter() throws Exception {
        // given
        List<Reservation> reservations = List.of(
                sampleReservation(1L, TEST_NAME, TEST_DATE, TEST_TIME_ID, TEST_START_AT, TEST_THEME_ID,
                        TEST_THEME_NAME),
                sampleReservation(2L, "리오", TEST_DATE.plusDays(1), 2L, "11:00", 2L, "테마B")
        );
        given(reservationService.findAll()).willReturn(reservations);

        // when & then
        mockMvc.perform(get("/reservations"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value(TEST_NAME))
                .andExpect(jsonPath("$[0].date").value(TEST_DATE_STR))
                .andExpect(jsonPath("$[1].name").value("리오"));

        then(reservationService).should().findAll();
    }

    @Test
    @DisplayName("GET /reservations - 예약이 하나도 없는 경우 빈 배열을 반환한다.")
    void readReservations_empty_list() throws Exception {
        given(reservationService.findAll()).willReturn(List.of());

        mockMvc.perform(get("/reservations"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    @DisplayName("GET /reservations?date=...&themeId=... - 예약 가능 시간 목록을 반환한다.")
    void readAvailableReservations_filter_by_date_and_theme() throws Exception {
        // given
        List<Reservation> reservations = List.of(
                sampleReservation(null, null, TEST_DATE, TEST_TIME_ID, TEST_START_AT, TEST_THEME_ID, TEST_THEME_NAME),
                sampleReservation(1L, TEST_NAME, TEST_DATE, 2L, "11:00", TEST_THEME_ID, TEST_THEME_NAME)
        );
        given(reservationService.findAllByDateAndThemeId(TEST_DATE, TEST_THEME_ID)).willReturn(reservations);

        // when & then
        mockMvc.perform(get("/reservations")
                        .param("date", TEST_DATE_STR)
                        .param("themeId", TEST_THEME_ID.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].time.id").value(TEST_TIME_ID))
                .andExpect(jsonPath("$[0].isAvailable").value(true))
                .andExpect(jsonPath("$[1].time.id").value(2))
                .andExpect(jsonPath("$[1].isAvailable").value(false));

        then(reservationService).should().findAllByDateAndThemeId(TEST_DATE, TEST_THEME_ID);
    }

    @Test
    @DisplayName("GET /reservations?name=... - 예약 목록을 이름 기반으로 조회 후 반환한다.")
    void readAvailableReservations_filter_by_name() throws Exception {
        // given
        List<Reservation> reservations = List.of(
                sampleReservation(1L, TEST_NAME, TEST_DATE, 2L, "11:00", TEST_THEME_ID, TEST_THEME_NAME));
        given(reservationService.findByName(TEST_NAME)).willReturn(reservations);

        // when & then
        mockMvc.perform(get("/reservations")
                        .param("name", TEST_NAME))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].name").value(TEST_NAME))
                .andExpect(jsonPath("$[0].date").value(TEST_DATE_STR))
                .andExpect(jsonPath("$[0].time.id").value(2))
                .andExpect(jsonPath("$[0].theme.id").value(TEST_THEME_ID));

        then(reservationService).should().findByName(TEST_NAME);
    }

    @Test
    @DisplayName("GET /reservations?name=... - 이름이 누락되면 400 에러를 반환한다.")
    void readAvailableReservations_fail_missing_name() throws Exception {
        mockMvc.perform(get("/reservations")
                        .param("name", ""))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorType").value("BUSINESS"))
                .andExpect(jsonPath("$.message").value("이름이 비어있습니다. 이름을 입력해주세요"));
    }

    @Test
    @DisplayName("GET /reservations - 서비스에서 예상치 못한 예외 발생 시 500 에러를 반환한다.")
    void readReservations_fail_internal_server_error() throws Exception {
        given(reservationService.findAll()).willThrow(new RuntimeException("DB 연결 실패"));

        mockMvc.perform(get("/reservations"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.errorType").value("SERVER"))
                .andExpect(jsonPath("$.message").value("서버 내부 오류 발생\n 관리자에게 문의해주세요"));
    }

    @Test
    @DisplayName("PATCH /reservations/{id}?name=... - 정상 수정 시 200과 수정된 예약 정보를 반환한다.")
    void updateReservation_success() throws Exception {
        // given
        Long id = 1L;
        LocalDate newDate = TEST_DATE.plusDays(1);
        Long newTimeId = 2L;

        Reservation updated = sampleReservation(id, TEST_NAME, newDate, newTimeId, "11:00", TEST_THEME_ID,
                TEST_THEME_NAME);
        given(reservationService.updateDateAndTime(id, TEST_NAME, newDate, newTimeId))
                .willReturn(updated);

        ReservationUpdateRequest request = new ReservationUpdateRequest(newDate, newTimeId);

        // when & then
        ResultActions actions = mockMvc.perform(patch("/reservations/{id}", id)
                        .param("name", TEST_NAME)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        verifyReservationResponse(actions, updated);
        then(reservationService).should().updateDateAndTime(id, TEST_NAME, newDate, newTimeId);
    }

    @Test
    @DisplayName("PATCH /reservations/{id} - 이름(name) 파라미터가 비어있으면 400 에러를 반환한다.")
    void updateReservation_fail_when_name_is_blank() throws Exception {
        Map<String, Object> requestInfo = new HashMap<>();
        requestInfo.put("timeId", TEST_TIME_ID);
        requestInfo.put("date", TEST_DATE_STR);

        // when & then
        mockMvc.perform(patch("/reservations/1")
                        .param("name", " ")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestInfo)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorType").value("BUSINESS"))
                .andExpect(jsonPath("$.message").value("이름이 비어있습니다. 이름을 입력해주세요"));
    }

    @Test
    @DisplayName("PATCH /reservations/{id} - 수정 시 날짜(date)가 누락되면 RESERVATION_UPDATE_DATE_EMPTY 에러를 반환한다.")
    void updateReservation_fail_missing_date() throws Exception {
        Map<String, Object> requestInfo = new HashMap<>();
        requestInfo.put("timeId", TEST_TIME_ID);
        requestInfo.put("date", null);

        mockMvc.perform(patch("/reservations/1")
                        .param("name", TEST_NAME)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestInfo)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorType").value("BUSINESS"))
                .andExpect(jsonPath("$.message").value("변경할 예약 날짜가 누락되었습니다. 날짜를 선택해주세요."));
    }

    @Test
    @DisplayName("PATCH /reservations/{id} - 수정 시 시간(timeId)이 누락되면 RESERVATION_UPDATE_TIME_ID_EMPTY 에러를 반환한다.")
    void updateReservation_fail_missing_timeId() throws Exception {
        Map<String, Object> requestInfo = new HashMap<>();
        requestInfo.put("timeId", null);
        requestInfo.put("date", TEST_DATE_STR);

        mockMvc.perform(patch("/reservations/1")
                        .param("name", TEST_NAME)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestInfo)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorType").value("BUSINESS"))
                .andExpect(jsonPath("$.message").value("변경할 예약 시간이 누락되었습니다. 시간을 선택해주세요."));
    }

    /**
     * DELETE /reservations/{id} - 예약 삭제
     */
    @Test
    @DisplayName("DELETE /reservations/{id} - 정상 삭제 시 204와 빈 본문을 반환한다.")
    void deleteReservation_success() throws Exception {
        // when & then
        mockMvc.perform(delete("/reservations/1").header("Authorization", "ADMIN"))
                .andExpect(status().isNoContent())
                .andExpect(content().string(""));

        then(reservationService).should().deleteById(1L);
    }

    @Test
    @DisplayName("DELETE /reservations/{id} - 관리자 권한 없이 삭제 요청 시 403 에러를 반환한다.")
    void deleteReservation_fail_without_admin() throws Exception {
        mockMvc.perform(delete("/reservations/1"))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("DELETE /reservations/{id} - ID가 숫자가 아닌 경우 400 에러를 반환한다.")
    void deleteReservation_fail_invalid_id_type() throws Exception {
        mockMvc.perform(delete("/reservations/abc")
                        .header("Authorization", "ADMIN"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorType").value("BUSINESS"))
                .andExpect(jsonPath("$.message").value("요청 정보가 올바르지 않습니다. 입력 내용을 다시 확인해주세요."));
    }

    @Test
    @DisplayName("DELETE /reservations/{id} - 존재하지 않는 예약 삭제 시 404 에러를 반환한다.")
    void deleteReservation_fail_not_found() throws Exception {
        willThrow(new NotFoundException(ErrorCode.RESERVATION_NOT_FOUND_BY_ID))
                .given(reservationService).deleteById(anyLong());

        mockMvc.perform(delete("/reservations/1")
                        .header("Authorization", "ADMIN"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorType").value("BUSINESS"))
                .andExpect(jsonPath("$.message").value("해당 번호의 예약 정보를 찾을 수 없습니다."));
    }

    @Test
    @DisplayName("PUT /reservations/{id} - 지원하지 않는 메서드 호출 시 405 에러를 반환한다.")
    void anyMethod_fail_unsupported_method() throws Exception {
        mockMvc.perform(put("/reservations/1"))
                .andExpect(status().isMethodNotAllowed())
                .andExpect(jsonPath("$.errorType").value("BUSINESS"))
                .andExpect(jsonPath("$.message").value("지원하지 않는 요청 방법입니다. 주소 혹은 요청 방식을 확인해주세요."));
    }

    private Reservation sampleReservation(Long id, String name, LocalDate date, Long timeId, String startAt,
                                          Long themeId, String themeName) {
        ReservationTime time = ReservationTime.createWithId(timeId, LocalTime.parse(startAt));
        Theme theme = Theme.createWithId(themeId, themeName, "설명", "https://thumbnail.com");
        return Reservation.createWithId(id, name, date, time, theme);
    }

    private void verifyReservationResponse(ResultActions actions, Reservation reservation) throws Exception {
        actions.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(reservation.id()))
                .andExpect(jsonPath("$.name").value(reservation.name()))
                .andExpect(jsonPath("$.date").value(reservation.date().toString()))
                .andExpect(jsonPath("$.time.id").value(reservation.time().id()))
                .andExpect(jsonPath("$.time.startAt").value(
                        reservation.time().startAt().format(DateTimeFormatter.ofPattern("HH:mm:ss"))))
                .andExpect(jsonPath("$.theme.id").value(reservation.theme().id()))
                .andExpect(jsonPath("$.theme.name").value(reservation.theme().name()));
    }
}
