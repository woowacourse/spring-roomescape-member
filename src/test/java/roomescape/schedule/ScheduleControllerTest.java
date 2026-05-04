package roomescape.schedule;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(ScheduleController.class)
class ScheduleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ScheduleService scheduleService;

    @Test
    @DisplayName("스케줄을 조회할 수 있다")
    void getSchedules() throws Exception {
        LocalDate date = LocalDate.of(2026, 5, 1);
        ScheduleResponse response = new ScheduleResponse(
                3L,
                date,
                List.of(
                        new AvailableTimeDto(1L, true),
                        new AvailableTimeDto(2L, false)
                )
        );

        when(scheduleService.getSchedules(eq(date), eq(3L))).thenReturn(response);

        mockMvc.perform(get("/schedules")
                        .param("date", "2026-05-01")
                        .param("themeId", "3")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.themeId").value(3))
                .andExpect(jsonPath("$.date").value("2026-05-01"))
                .andExpect(jsonPath("$.schedules[0].timeId").value(1))
                .andExpect(jsonPath("$.schedules[0].isAvailable").value(true))
                .andExpect(jsonPath("$.schedules[1].timeId").value(2))
                .andExpect(jsonPath("$.schedules[1].isAvailable").value(false));
    }
}

