package roomescape.presentation;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
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
import roomescape.presentation.dto.ReservationRequest;
import roomescape.presentation.dto.ReservationUpdateRequest;

@WebMvcTest(ReservationController.class)
@Import({AdminInterceptor.class, ReservationControllerTest.TestWebConfig.class})
class ReservationControllerTest {

    private static final LocalDate TEST_DATE = LocalDate.now();
    private static final String TEST_DATE_STR = TEST_DATE.toString();

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

    private void verifyReservationResponse(ResultActions actions, Reservation reservation) throws Exception {
        actions.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(reservation.id()))
                .andExpect(jsonPath("$.name").value(reservation.name()))
                .andExpect(jsonPath("$.date").value(reservation.date().toString()))
                .andExpect(jsonPath("$.time.id").value(reservation.time().id()))
                .andExpect(jsonPath("$.time.startAt").value(reservation.time().startAt().format(DateTimeFormatter.ofPattern("HH:mm:ss"))))
                .andExpect(jsonPath("$.theme.id").value(reservation.theme().id()))
                .andExpect(jsonPath("$.theme.name").value(reservation.theme().name()));
    }

    @Test
    @DisplayName("POST /reservations - 정상 저장 시 201과 응답 본문을 반환한다.")
    void createReservation_success() throws Exception {
        // given
        Reservation reservation = sampleReservation(1L, "브라운", TEST_DATE, 1L, "10:00", 1L, "테스트-테마");
        given(reservationService.save("브라운", TEST_DATE, 1L, 1L)).willReturn(reservation);

        ReservationRequest request = new ReservationRequest("브라운", TEST_DATE, 1L, 1L);

        // when & then
        ResultActions actions = mockMvc.perform(post("/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "http://localhost/reservations/1"));

        verifyReservationResponse(actions, reservation);
        then(reservationService).should().save("브라운", TEST_DATE, 1L, 1L);
    }

    @Test
    @DisplayName("GET /reservations - 필터 없이 호출하면 전체 목록을 반환한다.")
    void readReservations_no_filter() throws Exception {
        // given
        List<Reservation> reservations = List.of(
                sampleReservation(1L, "브라운", TEST_DATE, 1L, "10:00", 1L, "테마A"),
                sampleReservation(2L, "리오", TEST_DATE.plusDays(1), 2L, "11:00", 2L, "테마B")
        );
        given(reservationService.findAll()).willReturn(reservations);

        // when & then
        mockMvc.perform(get("/reservations"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("브라운"))
                .andExpect(jsonPath("$[0].date").value(TEST_DATE_STR))
                .andExpect(jsonPath("$[1].name").value("리오"));

        then(reservationService).should().findAll();
    }

    @Test
    @DisplayName("GET /reservations?date=...&themeId=... - 예약 가능 시간 목록을 반환한다.")
    void readAvailableReservations_filter_by_date_and_theme() throws Exception {
        // given
        List<Reservation> reservations = List.of(
                sampleReservation(null, null, TEST_DATE, 1L, "10:00", 1L, "테마A"),
                sampleReservation(1L, "브라운", TEST_DATE, 2L, "11:00", 1L, "테마A")
        );
        given(reservationService.findAllByDateAndThemeId(TEST_DATE, 1L)).willReturn(reservations);

        // when & then
        mockMvc.perform(get("/reservations")
                        .param("date", TEST_DATE_STR)
                        .param("themeId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].time.id").value(1))
                .andExpect(jsonPath("$[0].isAvailable").value(true))
                .andExpect(jsonPath("$[1].time.id").value(2))
                .andExpect(jsonPath("$[1].isAvailable").value(false));

        then(reservationService).should().findAllByDateAndThemeId(TEST_DATE, 1L);
    }

    @Test
    @DisplayName("GET /reservations?name=... - 예약 목록을 이름 기반으로 조회 후 반환한다.")
    void readAvailableReservations_filter_by_name() throws Exception {
        // given
        List<Reservation> reservations = List.of(sampleReservation(1L, "브라운", TEST_DATE, 2L, "11:00", 1L, "테마A"));
        given(reservationService.findByName("브라운")).willReturn(reservations);

        // when & then
        mockMvc.perform(get("/reservations")
                        .param("name", "브라운"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].name").value("브라운"))
                .andExpect(jsonPath("$[0].date").value(TEST_DATE_STR))
                .andExpect(jsonPath("$[0].time.id").value(2))
                .andExpect(jsonPath("$[0].theme.id").value(1));

        then(reservationService).should().findByName("브라운");
    }

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
    @DisplayName("POST /reservations - 예약 시간이 누락된 경우 400 에러와 명확한 메시지를 반환한다.")
    void createReservation_fail_when_timeId_is_null() throws Exception {
        // given
        String body = "{\"name\":\"브라운\", \"date\":\"" + TEST_DATE_STR + "\", \"themeId\":1}";

        // when & then
        mockMvc.perform(post("/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("예약 시간이 비어있습니다. 시간을 선택해주세요."));
    }

    @Test
    @DisplayName("PATCH /reservations/{id}?name=... - 정상 수정 시 200과 수정된 예약 정보를 반환한다.")
    void updateReservation_success() throws Exception {
        // given
        Long id = 1L;
        String name = "브라운";
        LocalDate newDate = TEST_DATE.plusDays(1);
        Long newTimeId = 2L;

        Reservation updated = sampleReservation(id, name, newDate, newTimeId, "11:00", 1L, "테마A");
        given(reservationService.updateDateAndTime(id, name, Optional.of(newDate), Optional.of(newTimeId)))
                .willReturn(updated);

        ReservationUpdateRequest request = new ReservationUpdateRequest(Optional.of(newDate), Optional.of(newTimeId));

        // when & then
        ResultActions actions = mockMvc.perform(patch("/reservations/{id}", id)
                        .param("name", name)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        verifyReservationResponse(actions, updated);
        then(reservationService).should().updateDateAndTime(id, name, Optional.of(newDate), Optional.of(newTimeId));
    }

    @Test
    @DisplayName("PATCH /reservations/{id} - 이름(name) 파라미터가 비어있으면 400 에러를 반환한다.")
    void updateReservation_fail_when_name_is_blank() throws Exception {
        // when & then
        mockMvc.perform(patch("/reservations/1")
                        .param("name", " ")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("요청 이름이 비어있습니다. 이름을 입력해주세요"));
    }
}
