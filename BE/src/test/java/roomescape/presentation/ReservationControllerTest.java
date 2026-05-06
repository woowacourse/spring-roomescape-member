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
import java.time.LocalDate;
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
import roomescape.application.ReservationService;
import roomescape.entity.Reservation;
import roomescape.entity.ReservationTime;
import roomescape.entity.Theme;
import roomescape.global.auth.AdminInterceptor;

@WebMvcTest(ReservationController.class)
@Import({AdminInterceptor.class, ReservationControllerTest.TestWebConfig.class})
class ReservationControllerTest {

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

    private Reservation sampleReservation(Long id, String name, LocalDate date, Long timeId, String startAt,
                                          Long themeId, String themeName) {
        ReservationTime time = ReservationTime.createWithId(timeId, LocalTime.parse(startAt));
        Theme theme = Theme.createWithId(themeId, themeName, "설명", "https://thumbnail.com");
        return Reservation.createWithId(id, name, date, time, theme);
    }

    @Test
    @DisplayName("POST /reservations - 정상 저장 시 201과 응답 본문을 반환한다.")
    void createReservation_success() throws Exception {
        // given
        LocalDate date = LocalDate.of(2026, 5, 5);
        Reservation reservation = sampleReservation(1L, "브라운", date, 1L, "10:00", 1L, "테스트-테마");
        given(reservationService.saveReservation("브라운", date, 1L, 1L)).willReturn(reservation);

        Map<String, Object> body = new HashMap<>();
        body.put("name", "브라운");
        body.put("date", "2026-05-05");
        body.put("timeId", 1);
        body.put("themeId", 1);

        // when & then
        mockMvc.perform(post("/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/reservations/1"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("브라운"))
                .andExpect(jsonPath("$.date").value("2026-05-05"))
                .andExpect(jsonPath("$.time.id").value(1))
                .andExpect(jsonPath("$.time.startAt").value("10:00"))
                .andExpect(jsonPath("$.theme.id").value(1))
                .andExpect(jsonPath("$.theme.name").value("테스트-테마"));

        then(reservationService).should().saveReservation("브라운", date, 1L, 1L);
    }

    @Test
    @DisplayName("GET /reservations - 필터 없이 호출하면 전체 목록을 반환한다.")
    void readReservations_no_filter() throws Exception {
        // given
        List<Reservation> reservations = List.of(
                sampleReservation(1L, "브라운", LocalDate.of(2026, 5, 5), 1L, "10:00", 1L, "테마A"),
                sampleReservation(2L, "리오", LocalDate.of(2026, 5, 6), 2L, "11:00", 2L, "테마B")
        );
        given(reservationService.getReservations()).willReturn(reservations);

        // when & then
        mockMvc.perform(get("/reservations"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("브라운"))
                .andExpect(jsonPath("$[1].name").value("리오"));

        then(reservationService).should().getReservations();
    }

    @Test
    @DisplayName("GET /reservations?date=...&themeId=... - 예약 가능 시간 목록을 반환한다.")
    void readAvailableReservations_filter_by_date_and_theme() throws Exception {
        // given
        LocalDate date = LocalDate.of(2026, 5, 5);
        List<Reservation> reservations = List.of(
                sampleReservation(null, null, date, 1L, "10:00", 1L, "테마A"),
                sampleReservation(1L, "브라운", date, 2L, "11:00", 1L, "테마A")
        );
        given(reservationService.getReservationsByDateAndTheme(date, 1L)).willReturn(reservations);

        // when & then
        mockMvc.perform(get("/reservations")
                        .param("date", "2026-05-05")
                        .param("themeId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].time.id").value(1))
                .andExpect(jsonPath("$[0].isAvailable").value(true))
                .andExpect(jsonPath("$[1].time.id").value(2))
                .andExpect(jsonPath("$[1].isAvailable").value(false));

        then(reservationService).should().getReservationsByDateAndTheme(date, 1L);
    }

    @Test
    @DisplayName("DELETE /reservations/{id} - 정상 삭제 시 204와 빈 본문을 반환한다.")
    void deleteReservation_success() throws Exception {
        // when & then
        mockMvc.perform(delete("/reservations/1"))
                .andExpect(status().isNoContent())
                .andExpect(content().string(""));

        then(reservationService).should().deleteReservation(1L);
    }
}
