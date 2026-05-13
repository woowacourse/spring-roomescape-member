package roomescape.theme.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import roomescape.availability.service.AvailabilityService;
import roomescape.time.domain.ReservationTime;
import roomescape.theme.domain.Theme;
import roomescape.theme.exception.ThemeNotFoundException;
import roomescape.theme.service.ThemeService;
import roomescape.theme.service.dto.ThemeSaveServiceDto;

@WebMvcTest(ThemeController.class)
class ThemeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ThemeService themeService;

    @MockitoBean
    private AvailabilityService availabilityService;

    @Test
    void getAll() throws Exception {
        Theme theme1 = new Theme("이름1", "설명1", "https://img.test/1.png").withId(1L);
        Theme theme2 = new Theme("이름2", "설명2", "https://img.test/2.png").withId(2L);

        Mockito.when(themeService.getAll()).thenReturn(List.of(theme1, theme2));

        mockMvc.perform(get("/themes")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void create() throws Exception {
        Theme saved = new Theme("이름", "설명", "https://img.test/a.png").withId(1L);
        Mockito.when(themeService.create(Mockito.any(ThemeSaveServiceDto.class)))
                .thenReturn(saved);

        String requestBody = """
                {
                    "name": "이름",
                    "description": "설명",
                    "imageUrl": "https://img.test/a.png"
                }
                """;

        mockMvc.perform(post("/themes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated());
    }

    @Test
    void deleteById() throws Exception {
        Long id = 1L;

        mockMvc.perform(delete("/themes/{id}", id))
                .andExpect(status().isNoContent());

        Mockito.verify(themeService).deleteById(id);
    }

    @Test
    void deleteById_없으면_404() throws Exception {
        Long id = 999L;
        Mockito.doThrow(new ThemeNotFoundException(id))
                .when(themeService).deleteById(id);

        mockMvc.perform(delete("/themes/{id}", id))
                .andExpect(status().isNotFound());
    }

    @Test
    void getAvailableTimes() throws Exception {
        Long themeId = 1L;
        LocalDate date = LocalDate.of(2026, 5, 6);

        List<ReservationTime> times = List.of(
                new ReservationTime(1L, "10:00", "12:00"),
                new ReservationTime(2L, "12:00", "14:00")
        );
        Mockito.when(availabilityService.getAvailableTimes(themeId, date)).thenReturn(times);

        mockMvc.perform(get("/themes/{themeId}/times", themeId)
                        .queryParam("date", "2026-05-06")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void getAvailableTimes_잘못된_날짜형식이면_400() throws Exception {
        mockMvc.perform(get("/themes/{themeId}/times", 1L)
                        .queryParam("date", "2026/05/06")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }


    @Test
    void create_예상못한오류면_안전한_500_응답() throws Exception {
        Mockito.when(themeService.create(Mockito.any(ThemeSaveServiceDto.class)))
                .thenThrow(new RuntimeException("boom"));

        String requestBody = """
                {
                    "name": "이름",
                    "description": "설명",
                    "imageUrl": "https://img.test/a.png"
                }
                """;

        mockMvc.perform(post("/themes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.code").value("INTERNAL_SERVER_ERROR"))
                .andExpect(jsonPath("$.message").value("서버 오류가 발생했습니다. 잠시 후 다시 시도해주세요."));
    }
}
