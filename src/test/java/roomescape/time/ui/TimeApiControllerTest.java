package roomescape.time.ui;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import roomescape.auth.application.AuthService;
import roomescape.time.application.TimeService;
import roomescape.time.dto.AvailableTimeRequest;
import roomescape.time.dto.AvailableTimeResponse;
import roomescape.time.dto.TimeRequest;
import roomescape.time.dto.TimeResponse;

@WebMvcTest(TimeApiController.class)
class TimeApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TimeService timeService;

    @MockitoBean
    private AuthService authService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("시간을 등록할 수 있다")
    void test1() throws Exception {
        // given
        TimeRequest request = new TimeRequest(LocalTime.of(10, 0));
        TimeResponse response = new TimeResponse(1L, LocalTime.of(10, 0));

        when(timeService.add(any(TimeRequest.class))).thenReturn(response);

        // when & then
        mockMvc.perform(post("/times")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.startAt").value("10:00"));
    }

    @Test
    @DisplayName("전체 시간 목록을 조회할 수 있다")
    void test2() throws Exception {
        // given
        TimeResponse response = new TimeResponse(1L, LocalTime.of(10, 0));

        when(timeService.findAll()).thenReturn(List.of(response));

        // when & then
        mockMvc.perform(get("/times"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].startAt").value("10:00"));
    }

    @Test
    @DisplayName("특정 날짜와 테마에 대한 사용 가능한 시간을 조회할 수 있다")
    void test3() throws Exception {
        // given
        AvailableTimeResponse response = new AvailableTimeResponse(1L, LocalTime.of(10, 0), true);

        when(timeService.findByDateAndThemeId(any(AvailableTimeRequest.class)))
                .thenReturn(List.of(response));

        // when & then
        mockMvc.perform(get("/times/available")
                        .param("date", "2025-05-20")
                        .param("themeId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].timeId").value(1))
                .andExpect(jsonPath("$[0].startAt").value("10:00"));
    }

    @Test
    @DisplayName("시간을 삭제할 수 있다")
    void test4() throws Exception {
        // given
        doNothing().when(timeService).deleteById(1L);

        // when & then
        mockMvc.perform(delete("/times/1"))
                .andExpect(status().isNoContent());
    }
}
