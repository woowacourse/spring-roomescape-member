package roomescape.presentation;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
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
import roomescape.domain.ReservationTime;
import roomescape.global.auth.AdminInterceptor;

@WebMvcTest(ReservationTimeController.class)
@Import({AdminInterceptor.class, ReservationTimeControllerTest.TestWebConfig.class})
class ReservationTimeControllerTest {

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
        LocalTime startAt = LocalTime.of(10, 0);
        given(reservationTimeService.saveTime(startAt)).willReturn(ReservationTime.createRow(1L, startAt));

        Map<String, Object> body = new HashMap<>();
        body.put("startAt", "10:00");

        // when & then
        mockMvc.perform(post("/times")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/times/1"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.startAt").value("10:00"));

        then(reservationTimeService).should().saveTime(startAt);
    }

    @Test
    @DisplayName("GET /times - 예약 시간 전체 조회 요청을 서비스에 전달하고 200 응답을 반환한다.")
    void readReservationTimes_success() throws Exception {
        // given
        given(reservationTimeService.getTimes()).willReturn(
                List.of(ReservationTime.createRow(1L, LocalTime.of(10, 0)))
        );

        // when & then
        mockMvc.perform(get("/times"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].startAt").value("10:00"));

        then(reservationTimeService).should().getTimes();
    }

    @Test
    @DisplayName("DELETE /times/{id} - 예약 시간 삭제 요청을 서비스에 전달하고 204 응답을 반환한다.")
    void deleteReservationTime_success() throws Exception {
        // when & then
        mockMvc.perform(delete("/times/1"))
                .andExpect(status().isNoContent())
                .andExpect(content().string(""));

        then(reservationTimeService).should().deleteTime(1L);
    }
}
