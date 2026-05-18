package roomescape.presentation;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalTime;
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
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import roomescape.application.ReservationTimeService;
import roomescape.entity.ReservationTime;
import roomescape.global.auth.AdminInterceptor;
import roomescape.global.exception.ErrorCode;
import roomescape.global.exception.customException.ConflictException;

@WebMvcTest(ReservationTimeController.class)
@Import({AdminInterceptor.class, ReservationTimeControllerTest.TestWebConfig.class})
class ReservationTimeControllerTest {

    private static final LocalTime TEST_START_AT = LocalTime.of(10, 0);
    private static final String TEST_START_AT_STR = "10:00";
    private static final String TEST_START_AT_RESPONSE_STR = "10:00:00";

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
    private ReservationTimeService reservationTimeService;

    @Test
    @DisplayName("POST /times - 예약 시간 저장 요청을 서비스에 전달하고 201 응답을 반환한다.")
    void createReservationTimes_success() throws Exception {
        // given
        given(reservationTimeService.save(TEST_START_AT)).willReturn(ReservationTime.createWithId(1L, TEST_START_AT));

        Map<String, Object> body = new HashMap<>();
        body.put("startAt", TEST_START_AT_STR);

        // when & then
        mockMvc.perform(post("/times")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "http://localhost/times/1"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.startAt").value(TEST_START_AT_RESPONSE_STR));

        then(reservationTimeService).should().save(TEST_START_AT);
    }

    @Test
    @DisplayName("POST /times - 시작 시간이 누락된 경우 400 에러를 반환한다.")
    void createReservationTimes_fail_missing_startAt() throws Exception {
        Map<String, Object> body = new HashMap<>();
        body.put("startAt", null);

        mockMvc.perform(post("/times")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorType").value("BUSINESS"))
                .andExpect(jsonPath("$.message").value("예약 시간이 선택되지 않았습니다. 시간을 선택해주세요."));
    }

    @Test
    @DisplayName("GET /times - 예약 시간 전체 조회 요청을 서비스에 전달하고 200 응답을 반환한다.")
    void readReservationTimes_success() throws Exception {
        // given
        given(reservationTimeService.findAll()).willReturn(
                List.of(ReservationTime.createWithId(1L, TEST_START_AT))
        );

        // when & then
        mockMvc.perform(get("/times"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].startAt").value(TEST_START_AT_RESPONSE_STR));

        then(reservationTimeService).should().findAll();
    }

    @Test
    @DisplayName("DELETE /times/{id} - 예약 시간 삭제 요청을 서비스에 전달하고 204 응답을 반환한다.")
    void deleteReservationTime_success() throws Exception {
        // when & then
        mockMvc.perform(delete("/times/1"))
                .andExpect(status().isNoContent())
                .andExpect(content().string(""));

        then(reservationTimeService).should().deleteById(1L);
    }

    @Test
    @DisplayName("DELETE /times/{id} - 사용 중인 시간을 삭제하려 할 때 409 에러를 반환한다.")
    void deleteReservationTime_fail_already_used() throws Exception {
        willThrow(new ConflictException(ErrorCode.RESERVATION_TIME_ALREADY_USED))
                .given(reservationTimeService).deleteById(anyLong());

        mockMvc.perform(delete("/times/1"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.errorType").value("BUSINESS"))
                .andExpect(jsonPath("$.message").value("사용 중인 예약 시간은 삭제할 수 없습니다. 관련 예약을 먼저 삭제해주세요."));
    }
}
